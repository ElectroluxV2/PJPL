import {ChangeDetectionStrategy, Component} from '@angular/core';
import {DataService} from "../services/data.service";
import {map, Observable, shareReplay, throttleTime} from "rxjs";

interface Day {
  index: number;
  color: string;
  current: boolean;
}

interface Month {
  name: string;
  offset: number;
  year: number;
  index: number;
  days: Day[];
}

interface ViewRange {
  year: number;
  month: number;
}

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CalendarComponent {
  public readonly months$: Observable<Month[]>;

  constructor(public readonly dataService: DataService) {
    this.months$ = this.dataService.subjectsChanged$.pipe(
      throttleTime(100),
      map(() => this.makeCalendar()),
      shareReplay(1),
    );
  }

  private makeCalendar(): Month[] {
    const months = [];

    const formatter = new Intl.DateTimeFormat('pl', {month: 'long'});

    const beginDate = new Date(this.dataService.firstValuableDay);
    const endDate = new Date(this.dataService.lastValuableDay);

    const begin: ViewRange = {
      year: beginDate.getFullYear(),
      month: beginDate.getMonth()
    };

    const end: ViewRange = {
      year: endDate.getFullYear(),
      month: endDate.getMonth()
    };

    const now = new Date(Date.now());

    for (let year = begin.year; year <= end.year; year++) {
      const beginMonth = year === begin.year ? begin.month : 0;
      const endMonth = year === end.year ? end.month : 11;

      for (let month = beginMonth; month <= endMonth; month++) {
        const firstDayInMonth = new Date(year, month, 1);
        const monthOffset =  (firstDayInMonth.getDay() + 6) % 7;
        const lastDayInMonth = new Date(year, month + 1, 0);
        const daysInMonth = lastDayInMonth.getDate();

        months.push({
          name: formatter.format(new Date(year, month, 1)),
          offset: monthOffset,
          year,
          index: month,
          days: Array
            .from({length: daysInMonth})
            .map((value, index) => ({
              index: index + 1,
              color: this.dataService.subjectsByTimestamp.has(new Date(year, month, index + 1).valueOf()) ? 'red' : 'none',
              current: now.getDate() === index + 1 && now.getMonth() === month && now.getFullYear() === year
            }))
        })
      }
    }

    return months;
  }
}
