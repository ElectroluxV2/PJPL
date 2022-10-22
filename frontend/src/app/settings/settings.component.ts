import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {MatOptionSelectionChange} from "@angular/material/core";
import {DataService} from "../services/data.service";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SettingsComponent implements OnDestroy {
  private alive = true;
  public readonly selectedSemestersIds = Array.from(this.dataService.selectedSemestersIds);
  public readonly selectedStudiesIds = Array.from(this.dataService.selectedStudiesIds);
  public readonly selectedGroupsIds = Array.from(this.dataService.selectedGroupsIds);

  constructor(public readonly dataService: DataService) { }

  public ngOnDestroy(): void {
    this.alive = false;
  }

  public onSemesterSelectionChange({ source: { selected, value: semesterId } }: MatOptionSelectionChange<string>): void {
    if (selected) {
      this.dataService.selectedSemestersIds.add(semesterId);
    } else {
      this.dataService.selectedSemestersIds.delete(semesterId);
    }
  }

  public onStudySelectionChange({ source: { selected, value: studyId } }: MatOptionSelectionChange<string>): void {
    if (selected) {
      this.dataService.selectedStudiesIds.add(studyId);
    } else {
      this.dataService.selectedStudiesIds.delete(studyId);
    }
  }

  public onGroupSelectionChange({ source: { selected, value: groupId } }: MatOptionSelectionChange<string>): void {
    if (selected) {
      this.dataService.selectedGroupsIds.add(groupId);
    } else {
      this.dataService.selectedGroupsIds.delete(groupId);
    }
  }
}
