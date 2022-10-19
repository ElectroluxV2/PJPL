import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {DataService} from "../services/data.service";

@Component({
  selector: 'app-today',
  templateUrl: './today.component.html',
  styleUrls: ['./today.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TodayComponent implements OnInit {

  constructor(private readonly dataService: DataService) { }

  ngOnInit(): void {
  }

}
