import {ChangeDetectionStrategy, Component} from '@angular/core';
import {GroupService, GroupWithMetadata} from "../../services/group.service";
import {MatOptionSelectionChange} from "@angular/material/core";
import {ReplaySubject} from "rxjs";
import {MatBottomSheetRef} from "@angular/material/bottom-sheet";
import {Study} from "../../services/api.service";

@Component({
  selector: 'app-group-chooser',
  templateUrl: './group-chooser.component.html',
  styleUrls: ['./group-chooser.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GroupChooserComponent {
  public readonly selectedSemestersId$ = new ReplaySubject<string>(1);
  public readonly filteredStudies$ = new ReplaySubject<Study[]>(1);
  public readonly selectedStudyId$ = new ReplaySubject<string>(1);
  public readonly filteredGroups$ = new ReplaySubject<GroupWithMetadata[]>(1);


  constructor(
    public readonly groupService: GroupService,
    private sheet: MatBottomSheetRef<GroupChooserComponent>
  ) { }

  public close(): void {
    this.sheet.dismiss();
  }

  public onSemesterSelectionChange({ source: { selected, value } }: MatOptionSelectionChange<string>): void {
    if (!selected) {
      return;
    }

    this.filteredGroups$.next([]);
    this.selectedSemestersId$.next(value);
    this.filteredStudies$.next(this.groupService.availableStudies$.value.filter(s => s.id.includes(value)));
  }

  public onStudySelectionChange({ source: { selected, value } }: MatOptionSelectionChange<string>): void {
    if (!selected) {
      return;
    }

    this.selectedStudyId$.next(value);
    this.filteredGroups$.next(this.groupService.getGroups(value));
  }
}
