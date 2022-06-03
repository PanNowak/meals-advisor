import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {CacheInvalidationService} from 'app/modules/shared/cache-invalidation.service';
import {Unit} from 'app/modules/unit/models';
import {initializeUnitListCachingAction, invalidateUnitListAction, setCachedUnitListAction} from 'app/modules/unit/store';
import {Observable, of} from 'rxjs';
import {mergeAll, switchMap} from 'rxjs/operators';

@Injectable()
export class UnitCacheEffects {

  invalidateUnitList$ = createEffect(() =>
    this.actions$.pipe(
      ofType(initializeUnitListCachingAction),
      switchMap(action => this.handleUnitListCaching(action.unitListToCache))
    )
  );

  constructor(private actions$: Actions,
              private cacheInvalidationService: CacheInvalidationService) {}

  private handleUnitListCaching(units: Unit[]): Observable<Action> {
    return of(
      of(setCachedUnitListAction({unitListToCache: units})),
      this.cacheInvalidationService.delayedObservable(invalidateUnitListAction())
    ).pipe(mergeAll());
  }
}
