import {ChangeDetectionStrategy, Component} from '@angular/core';
import {DataService, Subject} from "../services/data.service";

interface Day {
  index: number;
  color: string;
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
  public months: Month[] = [];

  constructor(public readonly dataService: DataService) {
    this.dataService.subjects$.subscribe(subjects => this.makeCalendar(subjects));
  }

  private makeCalendar(subjects: Map<number, Subject[]>): void {
    this.months = [];

    const formatter = new Intl.DateTimeFormat('pl', {month: 'long'});

    const beginDate = new Date(this.dataService.begin);
    const endDate = new Date(this.dataService.end);

    const begin: ViewRange = {
      year: beginDate.getFullYear(),
      month: beginDate.getMonth()
    };

    const end: ViewRange = {
      year: endDate.getFullYear(),
      month: endDate.getMonth()
    };

    for (let year = begin.year; year <= end.year; year++) {
      const beginMonth = year === begin.year ? begin.month : 0;
      const endMonth = year === end.year ? end.month : 11;

      for (let month = beginMonth; month <= endMonth; month++) {
        const firstDayInMonth = new Date(year, month, 1);
        const monthOffset = firstDayInMonth.getDay() - 1;
        const lastDayInMonth = new Date(year, month + 1, 0);
        const daysInMonth = lastDayInMonth.getDate();

        this.months.push({
          name: formatter.format(new Date(year, month, 1)),
          offset: monthOffset,
          year,
          index: month,
          days: Array
            .from({length: daysInMonth})
            .map((value, index) => ({
              index: index + 1,
              color: subjects.has(new Date(year, month, index + 1).valueOf()) ? 'red' : 'none'
            }))
        })
      }
    }
  }
}
