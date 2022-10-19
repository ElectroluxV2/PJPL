import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../state/state";
import {getSemestersList} from "../state/data/data.selectors";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SettingsComponent implements OnDestroy {
  public readonly semesters$ = this.store.select(getSemestersList);
  private alive = true;

  constructor(private readonly store: Store<State>) { }

  public ngOnDestroy(): void {
    this.alive = false;
  }

}
