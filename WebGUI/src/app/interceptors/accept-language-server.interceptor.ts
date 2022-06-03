import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Inject, Injectable} from '@angular/core';
import {REQUEST} from '@nguniversal/express-engine/tokens';
import {Request} from 'express';
import {Observable} from 'rxjs';

@Injectable()
export class AcceptLanguageServerInterceptor implements HttpInterceptor {

  constructor(@Inject(REQUEST) private request: Request) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const headers = req.headers.append('Accept-Language', this.request.headers['accept-language']);
    // TODO think about other headers

    const decoratedRequest = req.clone({headers});
    return next.handle(decoratedRequest);
  }
}
