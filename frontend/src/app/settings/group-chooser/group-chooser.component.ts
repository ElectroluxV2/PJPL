import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {GroupService} from "../../services/group.service";

@Component({
  selector: 'app-group-chooser',
  templateUrl: './group-chooser.component.html',
  styleUrls: ['./group-chooser.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GroupChooserComponent implements OnInit {

  constructor(private readonly groupService: GroupService) { }

  ngOnInit(): void {
  }

  add() {
    this.groupService.selectGroup("g1");
  }
}
