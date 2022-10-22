import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {MatOptionSelectionChange} from "@angular/material/core";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SettingsComponent implements OnDestroy {
  private alive = true;

  public ngOnDestroy(): void {
    this.alive = false;
  }

  public onSemesterSelectionChange({ source: { selected, value: semesterId } }: MatOptionSelectionChange<string>): void {

  }
}
