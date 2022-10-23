import {ChangeDetectionStrategy, Component} from '@angular/core';
import {DataService, Subject} from "../services/data.service";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {combineLatest} from "rxjs/internal/observable/combineLatest";

@Component({
  selector: 'app-today',
  templateUrl: './today.component.html',
  styleUrls: ['./today.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TodayComponent {
  public subjects: Subject[] = [];

  constructor(private readonly dataService: DataService, readonly route: ActivatedRoute) {
    combineLatest([route.paramMap, dataService.subjects$]).subscribe(([paramMap, subjects]) => this.loadDay(paramMap, subjects));
  }

  private loadDay(paramMap: ParamMap, subjectsMap: Map<number, Subject[]>) {
    if (!paramMap.has('year')) {
      // Tu kiedyś dziś
      return;
    }

    const dayKey = this.dataService.makeKey(
      Number(paramMap.get('year')),
      Number(paramMap.get('month')),
      Number(paramMap.get('day'))
    );

    this.subjects = subjectsMap.get(dayKey)!;

    console.log(this.subjects)
  }

  iterate(additionalData: Record<string, string>) {
    return Object.entries(additionalData);
  }
}
