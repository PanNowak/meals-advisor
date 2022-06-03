import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Inject, Injectable} from '@angular/core';
import {API_BASE_URL} from 'app/app.tokens';
import {Observable} from 'rxjs';

@Injectable()
export class BaseUrlInterceptor implements HttpInterceptor {

  constructor(@Inject(API_BASE_URL) private apiBaseUrl: string) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const prefixedUrl = `${this.apiBaseUrl}/${req.url}`;
    const decoratedRequest = req.clone({url: prefixedUrl});
    return next.handle(decoratedRequest);
  }
}
