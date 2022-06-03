import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {MealSummary, MealType} from 'app/modules/meal/models';
import {
  initializeMealListCachingAction,
  initializeMealTypeListCachingAction,
  invalidateMealListAction,
  invalidateMealTypeListAction,
  setCachedMealListAction,
  setCachedMealTypeListAction
} from 'app/modules/meal/store';
import {CacheInvalidationService} from 'app/modules/shared/cache-invalidation.service';
import {Observable, of} from 'rxjs';
import {mergeAll, switchMap} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class MealCacheEffects {

  cacheMealList$ = createEffect(() =>
    this.actions$.pipe(
      ofType(initializeMealListCachingAction),
      switchMap(action => this.handleMealListCaching(action.mealListToCache))
    )
  );

  cacheMealTypeList$ = createEffect(() =>
    this.actions$.pipe(
      ofType(initializeMealTypeListCachingAction),
      switchMap(action => this.handleMealTypeListCaching(action.mealTypeListToCache))
    )
  );

  constructor(private actions$: Actions,
              private cacheInvalidationService: CacheInvalidationService) {
  }

  private handleMealListCaching(meals: MealSummary[]): Observable<Action> {
    return of(
      of(setCachedMealListAction({mealListToCache: meals})),
      this.cacheInvalidationService.delayedObservable(invalidateMealListAction())
    ).pipe(mergeAll());
  }

  private handleMealTypeListCaching(mealTypes: MealType[]): Observable<Action> {
    return of(
      of(setCachedMealTypeListAction({mealTypeListToCache: mealTypes})),
      this.cacheInvalidationService.delayedObservable(invalidateMealTypeListAction())
    ).pipe(mergeAll());
  }
}
