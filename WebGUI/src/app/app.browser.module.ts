import {NgModule} from '@angular/core';
import {API_BASE_URL, CACHE_TTL} from 'app/app.tokens';

import {AppComponent} from 'app/components/app.component';
import {environment} from 'environments/environment';
import {AppModule} from './app.module';

@NgModule({
  imports: [
    AppModule
  ],
  providers: [
    {provide: API_BASE_URL, useValue: environment.apiPath},
    {provide: CACHE_TTL, useValue: environment.cache.ttlInSeconds * 1000}
  ],
  bootstrap: [AppComponent]
})
export class AppBrowserModule {
}
