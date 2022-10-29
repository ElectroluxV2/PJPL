import {ChangeDetectionStrategy, Component} from '@angular/core';
import {GroupService, GroupWithMetadata} from "../../services/group.service";
import {MatOptionSelectionChange} from "@angular/material/core";
import {BehaviorSubject, ReplaySubject} from "rxjs";
import {Study} from "../../services/data.service";
import {MatBottomSheetRef} from "@angular/material/bottom-sheet";

@Component({
  selector: 'app-group-chooser',
  templateUrl: './group-chooser.component.html',
  styleUrls: ['./group-chooser.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GroupChooserComponent {
  public readonly selectedSemestersIds$ = new BehaviorSubject<string[]>([]);
  public readonly filteredStudies$ = new ReplaySubject<Study[]>(1);
  public readonly selectedStudiesIds$ = new BehaviorSubject<string[]>([]);
  public readonly filteredGroups$ = new ReplaySubject<GroupWithMetadata[]>(1);


  constructor(
    public readonly groupService: GroupService,
    private sheet: MatBottomSheetRef<GroupChooserComponent>
  ) { }

  public close(): void {
    this.sheet.dismiss();
  }

  public onSemesterSelectionChange({ source: { selected, value } }: MatOptionSelectionChange<string>): void {
    const ids = this.selectedSemestersIds$.value;

    if (!selected) {
      this.selectedSemestersIds$.next(ids.filter(id => id !== value));
    } else {
      this.selectedSemestersIds$.next(ids.concat(value));
    }

    const filtered = this.groupService.availableStudies$.value.filter(s => this.selectedSemestersIds$.value.some(x => s.id.includes(x)));
    this.filteredStudies$.next(filtered);
    this.filteredGroups$.next([]);
  }

  public onStudySelectionChange({ source: { selected, value } }: MatOptionSelectionChange<string>): void {
    const ids = this.selectedStudiesIds$.value;

    if (!selected) {
      this.selectedStudiesIds$.next(ids.filter(id => id !== value));
    } else {
      this.selectedStudiesIds$.next(ids.concat(value));
    }

    const filtered: GroupWithMetadata[] = this.groupService.getGroups(this.selectedStudiesIds$.value);
    this.filteredGroups$.next(filtered);
  }
}
