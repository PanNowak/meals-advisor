import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {ServerModule, ServerTransferStateModule} from '@angular/platform-server';
import {AcceptLanguageServerInterceptor} from 'app/interceptors/accept-language-server.interceptor';
import {AppComponent} from 'app/components/app.component';
import {CACHE_TTL} from 'app/app.tokens';

import {AppModule} from './app.module';

@NgModule({
  imports: [
    AppModule,
    ServerModule,
    ServerTransferStateModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AcceptLanguageServerInterceptor, multi: true},
    {provide: CACHE_TTL, useValue: 0}
  ],
  bootstrap: [AppComponent],
})
export class AppServerModule {
}
