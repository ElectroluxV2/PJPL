import {ChangeDetectionStrategy, Component} from '@angular/core';
import {DataService} from "../services/data.service";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {combineLatest} from "rxjs/internal/observable/combineLatest";
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
  private daysSource: Day[] = [];
  public readonly days = new BehaviorSubject<Day[]>(this.daysSource);

  public disableScrollToCurrent = false;

  constructor(private readonly dataService: DataService, readonly route: ActivatedRoute) {
    combineLatest([route.paramMap, dataService.subjectsChanged$])
      .subscribe(([paramMap]) => this.loadDay(paramMap));
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
    this.daysSource = [(this.makeDay(dateWithoutTime))];
    this.days.next(this.daysSource);
  }
}
