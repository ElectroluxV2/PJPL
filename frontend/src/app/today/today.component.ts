import {ChangeDetectionStrategy, Component, ViewChild, ViewContainerRef} from '@angular/core';
import {DataService} from "../services/data.service";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {combineLatest} from "rxjs/internal/observable/combineLatest";
import {IInfiniteScrollEvent} from "ngx-infinite-scroll";
import {BehaviorSubject} from "rxjs";
import {Subject} from "../services/api.service";

interface Day {
  timestamp: number;
  current?: boolean;
  subjects?: (Subject & {current?: boolean})[];
}

@Component({
  selector: 'app-today',
  templateUrl: './today.component.html',
  styleUrls: ['./today.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TodayComponent {
  @ViewChild('list', {read: ViewContainerRef}) list?: ViewContainerRef;
  private readonly daysSource: Day[] = [];
  public readonly days = new BehaviorSubject<Day[]>(this.daysSource);

  private prependedDate!: Date;
  private appendedDate!: Date;

  public disableScrollToCurrent = false;

  constructor(private readonly dataService: DataService, readonly route: ActivatedRoute) {
    combineLatest([route.paramMap, dataService.subjectsChanged$])
      .subscribe(([paramMap]) => this.loadDay(paramMap));
  }

  private prevDate(date: Date): Date {
    const copy = new Date(date.valueOf());
    return new Date(copy.setDate(copy.getDate() - 1));
  }

  private nextDate(date: Date): Date {
    const copy = new Date(date.valueOf());
    return new Date(copy.setDate(copy.getDate() + 1));
  }

  private loadDay(paramMap: ParamMap): void {
    const now = new Date(Date.now())
    const requestedDateWithoutTime = new Date(now.getFullYear(), now.getMonth(), now.getDate());

    if (paramMap.has('year')) {
      requestedDateWithoutTime.setFullYear(Number(paramMap.get('year')));
      requestedDateWithoutTime.setMonth(Number(paramMap.get('month')));
      requestedDateWithoutTime.setDate(Number(paramMap.get('day')));

      // If requested date is different from now we shall not scroll to current subject
      if (
        now.getFullYear() !== requestedDateWithoutTime.getFullYear() ||
        now.getMonth() !== requestedDateWithoutTime.getMonth() ||
        now.getDate() !== requestedDateWithoutTime.getDate()
      ) {
        this.disableScrollToCurrent = true;
      }
    }

    this.appendDay(requestedDateWithoutTime);
    setTimeout(this.loadMoreDays.bind(this, requestedDateWithoutTime), 0);
  }

  private loadMoreDays(requestedDateWithoutTime: Date): void {
    this.list!.element.nativeElement.scrollTo(0, 1); // Auto scroll happen only when stick to top
    this.prependDay(this.prevDate(requestedDateWithoutTime));
    this.appendDay(this.nextDate(requestedDateWithoutTime));
  }

  private makeDay(dateWithoutTime: Date): Day {
    const timestamp = dateWithoutTime.valueOf();

    const subjects = this.dataService.subjectsByTimestamp.get(timestamp);

    if (!subjects) {
      return {
        timestamp,
        subjects
      }
    }

    const now = new Date(Date.now());

    const withDiffs = subjects.map(s => ({
      ...s,
      diff: Math.min(Math.abs(s.from - now.valueOf()), Math.abs(s.to - now.valueOf()))
    }));

    withDiffs.sort((a, b) => a.diff - b.diff);
    // Current subject is not always closest, we filter out subject that already completed.
    const min = withDiffs.filter(s => s.to >= now.valueOf())?.[0]?.diff ?? -1;
    withDiffs.sort((a, b) => a.from - b.from);

    return {
      timestamp,
      subjects: withDiffs.map(subject => ({
        ...subject,
        current:
          subject.diff === min &&
          dateWithoutTime.getDate() === now.getDate() &&
          dateWithoutTime.getFullYear() === now.getFullYear() &&
          dateWithoutTime.getMonth() === now.getMonth()
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

  public onScrollDown(ignored: IInfiniteScrollEvent): void {
    this.disableScrollToCurrent = true;
    this.appendDay(this.nextDate(this.appendedDate));
  }

  public onScrollUp(ignored: IInfiniteScrollEvent): void {
    this.disableScrollToCurrent = true;
    this.prependDay(this.prevDate(this.prependedDate));
  }
}
