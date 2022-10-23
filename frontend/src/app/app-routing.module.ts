import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CalendarComponent} from "./calendar/calendar.component";
import {TodayComponent} from "./today/today.component";
import {NotFoundComponent} from "./not-found/not-found.component";
import {SettingsComponent} from "./settings/settings.component";

const routes: Routes = [{
  path: 'calendar',
  component: CalendarComponent
}, {
  path: 'today/:year/:month/:day',
  component: TodayComponent
}, {
  path: 'today',
  component: TodayComponent
}, {
  path: 'settings',
  component: SettingsComponent
}, {
  path: '',
  pathMatch: 'full',
  redirectTo: 'calendar'
}, {
  path: '**',
  component: NotFoundComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    scrollPositionRestoration: 'enabled'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
