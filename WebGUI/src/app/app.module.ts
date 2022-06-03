import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {TransferHttpCacheModule} from '@nguniversal/common';

import {AppComponent} from 'app/components/app.component';
import {BaseUrlInterceptor} from 'app/interceptors/base-url.interceptor';
import {ResultHandlingModule} from 'app/modules/result-handling/result-handling.module';
import {RoutingModule} from 'app/modules/routing/routing.module';
import {SharedModule} from 'app/modules/shared/shared.module';
import {TaskCounterInterceptor} from 'app/interceptors/task-counter.interceptor';
import {environment} from 'environments/environment';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    SharedModule,
    ResultHandlingModule,
    RoutingModule,
    BrowserModule.withServerTransition({appId: 'serverApp'}),
    TransferHttpCacheModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatSidenavModule,
    HttpClientModule,
    StoreModule.forRoot({}),
    EffectsModule.forRoot([]),
    StoreDevtoolsModule.instrument({maxAge: 25, logOnly: environment.production}),
    MatProgressSpinnerModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: TaskCounterInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: BaseUrlInterceptor, multi: true}
  ]
})
export class AppModule {
}
