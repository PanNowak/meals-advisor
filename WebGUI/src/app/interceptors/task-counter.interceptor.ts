import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {
  decrementBackgroundTaskCount,
  incrementBackgroundTaskCount
} from 'app/modules/shared/store/background-task-count/background-task-count.actions';
import {Observable} from 'rxjs';
import {finalize} from 'rxjs/operators';

@Injectable()
export class TaskCounterInterceptor implements HttpInterceptor {

  constructor(private store: Store<AppState>) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.incrementTaskCount();

    return next.handle(req)
      .pipe(finalize(() => this.decrementTaskCountWithDefensiveDelay()));
  }

  private incrementTaskCount(): void {
    this.store.dispatch(incrementBackgroundTaskCount());
  }

  private decrementTaskCountWithDefensiveDelay(): void {
    setTimeout(() => this.store.dispatch(decrementBackgroundTaskCount()), 100);
  }
}
