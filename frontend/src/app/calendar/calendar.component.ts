import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent implements OnInit {
  public readonly months = Array.from({length: 1000}).map((v, i) => ({
    name: `Month #${i}`,
    days: Array.from({length: 31}).fill(0)
  }));

  constructor() { }

  ngOnInit(): void {
  }

}
