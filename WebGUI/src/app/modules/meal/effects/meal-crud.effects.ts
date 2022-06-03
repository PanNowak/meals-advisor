import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {Meal, MealSummary} from 'app/modules/meal/models';
import {MealService} from 'app/modules/meal/services/meal.service';
import {
  getCachedMealList,
  getMealCacheState,
  getMealTypeCacheState,
  isCurrentMealIdenticalToLoaded,
  mealDeleteFailure,
  mealDeleteRequest,
  mealDeleteSuccess,
  mealDetailsAddFailure,
  mealDetailsAddRequest,
  mealDetailsAddSuccess,
  mealDetailsFetchFailure,
  mealDetailsFetchSuccess,
  mealDetailsLoadRequest,
  mealDetailsUpdateFailure,
  mealDetailsUpdateRequest,
  mealDetailsUpdateSuccess,
  mealListFetchFailure,
  mealListFetchRequest,
  mealListFetchSuccess,
  mealListLoadRequest,
  mealTypeListFetchFailure,
  mealTypeListFetchRequest,
  mealTypeListFetchSuccess,
  mealTypeListLoadRequest,
  setMealListAction,
  setMealTypeListAction
} from 'app/modules/meal/store';
import {Observable, of} from 'rxjs';
import {catchError, concatMap, exhaustMap, first, map, mapTo, switchMap, take} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class MealCrudEffects {

  loadMealList$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealListLoadRequest),
      switchMap(() => this.handleMealListLoad())
    )
  );

  fetchMealList$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealListFetchRequest),
      switchMap(() => this.handleMealListFetch())
    )
  );

  loadMealTypeList$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealTypeListLoadRequest),
      switchMap(() => this.handleMealTypeListLoad())
    )
  );

  fetchMealTypeList$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealTypeListFetchRequest),
      switchMap(() => this.handleMealTypeListFetch())
    )
  );

  loadMealDetails$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealDetailsLoadRequest),
      switchMap(action => this.handleMealDetailsLoad(action.mealId))
    )
  );

  addMeal$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealDetailsAddRequest),
      exhaustMap(action => this.handleMealAdd(action.mealToAdd))
    )
  );

  updateMeal$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealDetailsUpdateRequest),
      exhaustMap(action => this.handleMealUpdate(action.mealToUpdate))
    )
  );

  deleteMeal$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealDeleteRequest),
      exhaustMap(action => this.handleMealDeletion(action.mealToDelete))
    )
  );

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private mealService: MealService) {}

  private handleMealListLoad(): Observable<Action> {
    return this.store.select(getMealCacheState).pipe(
      take(1),
      map(mealCacheState => {
        if (mealCacheState.isInvalidated) {
          return mealListFetchRequest();
        } else {
          const cachedMealList = getCachedMealList(mealCacheState);
          return setMealListAction({meals: cachedMealList});
        }
      })
    );
  }

  private handleMealListFetch(): Observable<Action> {
    return this.mealService.getAll()
      .pipe(
        map(mealList => mealListFetchSuccess({meals: mealList})),
        catchError(err => of(mealListFetchFailure({error: err})))
      );
  }

  private handleMealTypeListLoad(): Observable<Action> {
    return this.store.select(getMealTypeCacheState).pipe(
      take(1),
      map(mealTypeCacheState => {
        if (mealTypeCacheState.isInvalidated) {
          return mealTypeListFetchRequest();
        } else {
          const cachedMealTypeList = getCachedMealList(mealTypeCacheState);
          return setMealTypeListAction({mealTypes: cachedMealTypeList});
        }
      })
    );
  }

  private handleMealTypeListFetch(): Observable<Action> {
    return this.mealService.getAllTypes()
      .pipe(
        map(mealTypeList => mealTypeListFetchSuccess({mealTypes: mealTypeList})),
        catchError(err => of(mealTypeListFetchFailure({error: err})))
      );
  }

  private handleMealDetailsLoad(mealId: number): Observable<Action> {
    return this.mealService.getById(mealId)
      .pipe(
        map(meal => mealDetailsFetchSuccess({loadedMeal: meal})),
        catchError(err => of(mealDetailsFetchFailure({error: err})))
      );
  }

  private handleMealAdd(mealToAdd: Meal): Observable<Action> {
    return this.mealService.create(mealToAdd)
      .pipe(
        map(meal => mealDetailsAddSuccess({addedMeal: meal})),
        catchError(err => of(mealDetailsAddFailure({error: err})))
      );
  }

  private handleMealUpdate(mealToUpdate: Meal): Observable<Action> {
    return this.store.select(isCurrentMealIdenticalToLoaded)
      .pipe(
        first(),
        concatMap(isIdentical => isIdentical ?
          [mealDetailsUpdateSuccess({updatedMeal: mealToUpdate})] :
          this.doUpdateMeal(mealToUpdate)
        )
      );
  }

  private doUpdateMeal(mealToUpdate: Meal): Observable<Action> {
    return this.mealService.update(mealToUpdate.id, mealToUpdate)
      .pipe(
        map(meal => mealDetailsUpdateSuccess({updatedMeal: meal})),
        catchError(err => of(mealDetailsUpdateFailure({error: err})))
      );
  }

  private handleMealDeletion(meal: MealSummary): Observable<Action> {
    return this.mealService.deleteById(meal.id)
      .pipe(
        mapTo(mealDeleteSuccess({deletedMeal: meal})),
        catchError(err => of(mealDeleteFailure({error: err})))
      );
  }
}
