import {ChangeDetectionStrategy, Component} from '@angular/core';
import {DataService} from "../services/data.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-today',
  templateUrl: './today.component.html',
  styleUrls: ['./today.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TodayComponent {

  constructor(private readonly dataService: DataService, readonly route: ActivatedRoute) {
    route.paramMap.subscribe(params => this.loadDay(params.get('year')!, params.get('month')!, params.get('day')!))
  }

  private loadDay(year?: string, month?: string, day?: string) {
    if (!year) return;

    console.log(year, month, day)
  }
}
