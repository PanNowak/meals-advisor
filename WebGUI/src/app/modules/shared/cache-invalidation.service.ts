import {ApplicationRef, Inject, Injectable} from '@angular/core';
import {Action} from '@ngrx/store';
import {CACHE_TTL} from 'app/app.tokens';
import {Observable, of} from 'rxjs';
import {delay, first, switchMap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CacheInvalidationService {

  constructor(private appRef: ApplicationRef,
              @Inject(CACHE_TTL) private cacheTtl: number) {}

  delayedObservable(action: Action): Observable<Action> {
    return this.appRef.isStable.pipe(
      first(stable => stable),
      switchMap(() => of(action).pipe(delay(this.cacheTtl)))
    );
  }
}
