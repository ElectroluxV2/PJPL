import {ChangeDetectionStrategy, Component} from '@angular/core';
import {DataService, Subject} from "../services/data.service";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {combineLatest} from "rxjs/internal/observable/combineLatest";
import {IInfiniteScrollEvent} from "ngx-infinite-scroll";
import {BehaviorSubject} from "rxjs";

interface Day {
  index: number;
  subjects: Subject[];
}

@Component({
  selector: 'app-today',
  templateUrl: './today.component.html',
  styleUrls: ['./today.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TodayComponent {
  public readonly days = new BehaviorSubject<Day[]>([]);

  constructor(private readonly dataService: DataService, readonly route: ActivatedRoute) {
    combineLatest([route.paramMap, dataService.subjects$]).subscribe(([paramMap, subjects]) => this.loadDay(paramMap, subjects));
  }

  private loadDay(paramMap: ParamMap, subjectsMap: Map<number, Subject[]>) {

    console.log(paramMap)

    if (!paramMap.has('year')) {
      // Tu kiedyś dziś
      return;
    }

    const dayKey = this.dataService.makeKey(
      Number(paramMap.get('year')),
      Number(paramMap.get('month')),
      Number(paramMap.get('day'))
    );

    const subjects = subjectsMap.get(dayKey)!;

    this.days.next([{
      index: 0,
      subjects
    }]);
  }

  iterate(additionalData: Record<string, string>) {
    return Object.entries(additionalData);
  }

  onScrollDown(event: IInfiniteScrollEvent) {
    console.log(event);
  }

  onScrollUp(event: IInfiniteScrollEvent) {
    console.log(event);
  }
}
