import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {Store} from "@ngrx/store";
import {State} from "./state/state";
import {loadSemesters} from "./state/data/data.actions";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent {

  constructor(private readonly store: Store<State>) {
    this.store.dispatch(loadSemesters());
  }

  public isActive(button: MatButton) {
    return button._elementRef.nativeElement.classList.contains('active');
  }
}
