import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {filter, map, tap} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent {
  // public readonly todayText = of('a');
  public readonly todayText = this.router.events.pipe(
    filter(event => event instanceof NavigationEnd),
    map(event => (event as NavigationEnd).url),
    tap(m => console.log(m)),
  );

  constructor(private readonly router: Router, private readonly route: ActivatedRoute) {
  }

  public isActive(button: MatButton) {
    return button._elementRef.nativeElement.classList.contains('active');
  }
}
