import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../state/state";
import {getSelectedSemestersIds, getSemestersList, getStudies, getStudiesList} from "../state/data/data.selectors";
import {MatOptionSelectionChange} from "@angular/material/core";
import {addSemesterToSelected, removeSemesterFromSelected} from "../state/data/data.actions";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SettingsComponent implements OnDestroy {
  public readonly semesters$ = this.store.select(getSemestersList);
  public readonly selectedSemestersIds$ = this.store.select(getSelectedSemestersIds);
  public readonly studies$ = this.store.select(getStudiesList);
  private alive = true;

  constructor(private readonly store: Store<State>) {
    this.store.select(getStudies).subscribe(s => console.log(s));
  }

  public ngOnDestroy(): void {
    this.alive = false;
  }

  public onSemesterSelectionChange({ source: { selected, value: semesterId } }: MatOptionSelectionChange<string>): void {
    selected ?
      this.store.dispatch(addSemesterToSelected({ semesterId })) :
      this.store.dispatch(removeSemesterFromSelected({ semesterId }))
  }
}
