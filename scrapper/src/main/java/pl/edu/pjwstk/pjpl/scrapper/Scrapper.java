package pl.edu.pjwstk.pjpl.scrapper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import picocli.CommandLine;
import pl.edu.pjwstk.pjpl.scrapper.components.GroupSchedulePage;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;
import static pl.edu.pjwstk.pjpl.scrapper.Utils.*;

@Command(name = "scrap", mixinStandardHelpOptions = true, description = "Scraps PJATK schedule to JSON files. Set storage root by setting 'PJPL.storage' env.")
public class Scrapper implements Callable<Integer> {
    public static final Logger LOGGER = Logger.getLogger(Scrapper.class.getName());
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
        LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
    }

    @Option(names = {"-se", "--semester"}, description = "Scrap only semesters that contains this string. If set to 'current' automatically filters semesters by current year.")
    private String semesterFilter = "";//"2022/2023 zimowy";
    @Option(names = {"-st", "--study"}, description = "Scrap only studies that contains this string.")
    private String studyFilter = "";//"Informatyka niestacjonarne Gdańsk";
    @Option(names = {"-gr", "--group"}, description = "Scrap only groups that contains this string.")
    private String groupFilter = "";//"GIn I.5 - 54c";

    public static void main(final String[] args) {
        System.setProperty("webdriver.chrome.driver", System.getenv("PJPL_webdriver"));
        int exitCode = new CommandLine(new Scrapper()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        LOGGER.info("Welcome to PJPL Scrapper!");

        final var driver = SeleniumFactory.makeDriver();
        final var wait = SeleniumFactory.makeWait(driver);

        Integer result = 1;
        try {
            result = logic(driver, wait);
        } catch (final Exception exception) {
            exception.printStackTrace(System.err);
        } finally {
            driver.quit();
        }

        LOGGER.info("PJPL Scrapper is done with that.");
        return result;
    }

    public boolean filterMatch(final String value, final String filter) {
        final var lower = value.toLowerCase();
        return Arrays.stream(filter.toLowerCase().split(",")).allMatch(lower::contains);
    }

    public Integer logic(final WebDriver driver, final WebDriverWait wait) throws IOException {
        final var schedulePage = GroupSchedulePage.open(driver, wait);

        LOGGER.info("Scrapping, filters: semester(`%s`), study(`%s`), group(`%s`).".formatted(semesterFilter, studyFilter, groupFilter));

        final var semesterSelector =  schedulePage
                .openSemesterSelector();

        final var semestersToScrap = semesterSelector
                .listAvailableSemesters()
                .stream()
                .filter(semesterName -> semesterFilter.isEmpty() || filterMatch(semesterName, semesterFilter))
                .toList();

        final var semestersIndexFile = getStorage("semesters");
        updateIndex(semestersToScrap, semestersIndexFile);

        final var id = new AtomicInteger();
        for (int semesterIndex = 0, semestersToScrapSize = semestersToScrap.size(); semesterIndex < semestersToScrapSize; semesterIndex++) {
            final var semester = semestersToScrap.get(semesterIndex);
            schedulePage
                    .openSemesterSelector()
                    .chooseSemester(semester);

            final var studySelector = schedulePage
                    .openStudySelector();

            final var studiesToScrap = studySelector
                    .listAvailableStudies()
                    .stream()
                    .filter(studyName -> studyFilter.isEmpty() || filterMatch(studyName, studyFilter))
                    .toList();

            final var studiesIndexFile = getStorage(semester, "studies");
            updateIndex(studiesToScrap, studiesIndexFile);

            for (int studyIndex = 0, studiesToScrapSize = studiesToScrap.size(); studyIndex < studiesToScrapSize; studyIndex++) {
                final var study = studiesToScrap.get(studyIndex);
                schedulePage
                        .openStudySelector()
                        .chooseStudy(study);

                final var groupsToScrap = schedulePage
                        .getGroupSelector()
                        .listAvailableGroups()
                        .stream()
                        .filter(groupName -> groupFilter.isEmpty() ||  filterMatch(groupName, groupFilter))
                        .toList();

                final var groupsIndexFile = getStorage(semester, study, "groups");
                updateIndex(groupsToScrap, groupsIndexFile);

                LOGGER.info("Scrapping semester [%d/%d] (`%s`), study [%d/%d] (`%s`), filters resolved the following targets:%n  semesters:%n%s%n  studies:%n%s%n  groups:%n%s".formatted(
                        semesterIndex + 1, semestersToScrapSize, semester,
                        studyIndex + 1, studiesToScrapSize, study,
                        makeMultiLineList(semestersToScrap), makeMultiLineList(studiesToScrap), makeMultiLineList(groupsToScrap)
                ));

                final var scrappers = groupsToScrap
                        .stream()
                        .map(group -> new GroupScrapper(semester, study, group, id.incrementAndGet()));

                try (final var service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
                    scrappers.forEach(service::submit);
                }
            }
        }

        return 0;
    }
}
