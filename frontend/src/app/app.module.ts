import {isDevMode, LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ServiceWorkerModule} from '@angular/service-worker';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {CalendarComponent} from './calendar/calendar.component';
import {TodayComponent} from './today/today.component';
import {MatLineModule, MatRippleModule} from "@angular/material/core";
import {HttpClientModule} from "@angular/common/http";
import {SettingsComponent} from './settings/settings.component';
import {NotFoundComponent} from './not-found/not-found.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ScrollToCurrentDirective} from './scroll-to-current.directive';
import {GroupChooserComponent} from './settings/group-chooser/group-chooser.component';
import {MatBottomSheetModule} from "@angular/material/bottom-sheet";
import {registerLocaleData} from "@angular/common";
import localePl from '@angular/common/locales/pl';
import {MatButtonModule} from "@angular/material/button";
import {MatListModule} from "@angular/material/list";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {MatCardModule} from "@angular/material/card";
import {MatChipsModule} from "@angular/material/chips";
import { GdprComponent } from './gdpr/gdpr.component';
registerLocaleData(localePl);

@NgModule({
  declarations: [
    AppComponent,
    CalendarComponent,
    TodayComponent,
    SettingsComponent,
    NotFoundComponent,
    ScrollToCurrentDirective,
    GroupChooserComponent,
    GdprComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: !isDevMode(),
      // Register the ServiceWorker as soon as the application is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    }),
    BrowserAnimationsModule,
    HttpClientModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatLineModule,
    MatRippleModule,
    MatListModule,
    MatFormFieldModule,
    MatSelectModule,
    ReactiveFormsModule,
    FormsModule,
    MatCardModule,
    MatChipsModule,
    MatBottomSheetModule
  ],
  providers: [{
    provide: LOCALE_ID,
    useValue: 'pl-PL'
  }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
