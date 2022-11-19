import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ServiceWorkerModule} from '@angular/service-worker';
import {environment} from '../environments/environment';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatLegacyButtonModule as MatButtonModule} from "@angular/material/legacy-button";
import {CalendarComponent} from './calendar/calendar.component';
import {TodayComponent} from './today/today.component';
import {MatLineModule, MatRippleModule} from "@angular/material/core";
import {HttpClientModule} from "@angular/common/http";
import {SettingsComponent} from './settings/settings.component';
import {NotFoundComponent} from './not-found/not-found.component';
import {MatLegacyListModule as MatListModule} from "@angular/material/legacy-list";
import {MatLegacyFormFieldModule as MatFormFieldModule} from "@angular/material/legacy-form-field";
import {MatLegacySelectModule as MatSelectModule} from "@angular/material/legacy-select";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatLegacyCardModule as MatCardModule} from "@angular/material/legacy-card";
import {registerLocaleData} from "@angular/common";
import localePl from '@angular/common/locales/pl';
import {InfiniteScrollModule} from "ngx-infinite-scroll";
import { ScrollToCurrentDirective } from './scroll-to-current.directive';
import {MatLegacyChipsModule as MatChipsModule} from "@angular/material/legacy-chips";
import { GroupChooserComponent } from './settings/group-chooser/group-chooser.component';
import {MatBottomSheetModule} from "@angular/material/bottom-sheet";

registerLocaleData(localePl);

@NgModule({
  declarations: [
    AppComponent,
    CalendarComponent,
    TodayComponent,
    SettingsComponent,
    NotFoundComponent,
    ScrollToCurrentDirective,
    GroupChooserComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: environment.production,
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
    InfiniteScrollModule,
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
