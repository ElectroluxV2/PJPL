import {ChangeDetectionStrategy, Component} from '@angular/core';
import {DataService, Subject} from "../services/data.service";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {combineLatest} from "rxjs/internal/observable/combineLatest";
import {IInfiniteScrollEvent} from "ngx-infinite-scroll";
import {BehaviorSubject} from "rxjs";

interface Day {
  timestamp: number;
  subjects?: (Subject & {current?: boolean})[];
}

@Component({
  selector: 'app-today',
  templateUrl: './today.component.html',
  styleUrls: ['./today.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TodayComponent {
  private readonly daysSource: Day[] = [];
  public readonly days = new BehaviorSubject<Day[]>(this.daysSource);

  private prependedDate!: Date;
  private appendedDate!: Date;

  constructor(private readonly dataService: DataService, readonly route: ActivatedRoute) {
    combineLatest([route.paramMap, dataService.subjects$]).subscribe(([paramMap, subjects]) => this.loadDay(paramMap, subjects));
  }

  private prevDate(date: Date): Date {
    const copy = new Date(date.valueOf());
    return new Date(copy.setDate(copy.getDate() - 1));
  }

  private nextDate(date: Date): Date {
    const copy = new Date(date.valueOf());
    return new Date(copy.setDate(copy.getDate() + 1));
  }

  private loadDay(paramMap: ParamMap, subjectsMap: Map<number, Subject[]>) {
    const now = new Date(Date.now())
    const requestedDateWithoutTime = new Date(now.getFullYear(), now.getMonth(), now.getDate());

    if (paramMap.has('year')) {
      requestedDateWithoutTime.setFullYear(Number(paramMap.get('year')));
      requestedDateWithoutTime.setMonth(Number(paramMap.get('month')));
      requestedDateWithoutTime.setDate(Number(paramMap.get('day')));
    }

    this.appendDay(requestedDateWithoutTime);
    this.prependDay(this.prevDate(requestedDateWithoutTime));
    this.appendDay(this.nextDate(requestedDateWithoutTime));
  }

  private makeDay(dateWithoutTime: Date): Day {
    const timestamp = dateWithoutTime.valueOf();

    const subjects = this.dataService.subjects$.value.get(timestamp);

    if (!subjects) {
      return {
        timestamp,
        subjects
      }
    }

    const now = new Date(Date.now());

    const withDiffs = subjects.map(s => ({
      ...s,
      diff: Math.min(Math.abs(s.from * 1000 - now.valueOf()), Math.abs(s.to * 1000 - now.valueOf()))
    }));

    withDiffs.sort((a, b) => a.diff - b.diff);
    const min = withDiffs[0].diff;

    return {
      timestamp,
      subjects: withDiffs.map(subject => ({
        ...subject,
        current: subject.diff === min && dateWithoutTime.getDate() === now.getDate() && dateWithoutTime.getFullYear() === now.getFullYear() && dateWithoutTime.getMonth() === now.getMonth()
      }))
    };
  }

  private appendDay(dateWithoutTime: Date): void {
    this.appendedDate = dateWithoutTime;
    this.daysSource.push(this.makeDay(dateWithoutTime));
    this.days.next(this.daysSource);
  }

  private prependDay(dateWithoutTime: Date): void {
    this.prependedDate = dateWithoutTime;
    this.daysSource.unshift(this.makeDay(dateWithoutTime));
    this.days.next(this.daysSource);
  }

  public iterate(additionalData: Record<string, string>): [string, string][] {
    return Object.entries(additionalData);
  }

  public onScrollDown(event: IInfiniteScrollEvent): void {
    this.appendDay(this.nextDate(this.appendedDate));
  }

  public onScrollUp(event: IInfiniteScrollEvent): void {
    this.prependDay(this.prevDate(this.prependedDate));
  }
}
