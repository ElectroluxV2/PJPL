import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {DataService} from "../services/data.service";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {GroupChooserComponent} from "./group-chooser/group-chooser.component";
import {GroupService} from "../services/group.service";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SettingsComponent implements OnDestroy {
  private alive = true;

  constructor(
    public readonly dataService: DataService,
    public readonly groupService: GroupService,
    private readonly matBottomSheet: MatBottomSheet
  ) { }

  public ngOnDestroy(): void {
    this.alive = false;
  }

  public openGroupChooser(): void {
    this.matBottomSheet.open(GroupChooserComponent);
  }
}
