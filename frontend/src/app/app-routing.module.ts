import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CalendarComponent} from "./calendar/calendar.component";
import {TodayComponent} from "./today/today.component";

const routes: Routes = [{
  path: 'calendar',
  component: CalendarComponent
}, {
  path: 'today',
  component: TodayComponent
}, {
  path: '**',
  pathMatch: 'full',
  redirectTo: 'calendar'
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
