import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ServiceWorkerModule} from '@angular/service-worker';
import {environment} from '../environments/environment';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {CalendarComponent} from './calendar/calendar.component';
import {TodayComponent} from './today/today.component';
import {MatLineModule, MatRippleModule} from "@angular/material/core";
import {HttpClientModule} from "@angular/common/http";
import {SettingsComponent} from './settings/settings.component';
import {NotFoundComponent} from './not-found/not-found.component';
import {MatListModule} from "@angular/material/list";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatCardModule} from "@angular/material/card";
import {registerLocaleData} from "@angular/common";
import localePl from '@angular/common/locales/pl';
import {InfiniteScrollModule} from "ngx-infinite-scroll";
import { ScrollToCurrentDirective } from './scroll-to-current.directive';

registerLocaleData(localePl);

@NgModule({
  declarations: [
    AppComponent,
    CalendarComponent,
    TodayComponent,
    SettingsComponent,
    NotFoundComponent,
    ScrollToCurrentDirective
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
    ],
  providers: [{
    provide: LOCALE_ID,
    useValue: 'pl-PL'
  }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
