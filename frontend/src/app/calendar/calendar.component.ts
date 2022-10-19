import {ChangeDetectionStrategy, Component} from '@angular/core';

interface Day {
  index: number;
  color: string;
}

interface Month {
  name: string;
  offset: number;
  year: number;
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
  public readonly months: Month[];

  constructor() {
    this.months = [];

    const begin: ViewRange = {
      year: 2022,
      month: 8
    };

    const end: ViewRange = {
      year: 2023,
      month: 5
    };

    const formatter = new Intl.DateTimeFormat('pl', {month: 'long'});

    for (let year = begin.year; year <= end.year; year++) {
      const beginMonth = year === begin.year ? begin.month : 0;
      const endMonth = year === end.year ? end.month : 11;

      for (let month = beginMonth; month <= endMonth; month++) {
        const firstDayInMonth = new Date(year, month, 100);
        const monthOffset = firstDayInMonth.getDay();
        const lastDayInMonth = new Date(year, month, 0);
        const daysInMonth = lastDayInMonth.getDate();

        this.months.push({
          name: formatter.format(lastDayInMonth),
          offset: monthOffset,
          year,
          days: Array
            .from({length: daysInMonth})
            .map((value, index) => ({
              index,
              color: 'red'
            }))
        })
      }
    }
  }
}
