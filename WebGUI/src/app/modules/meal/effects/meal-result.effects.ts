import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {
  addMealToCachedListAction,
  cancelMealDetailsUpdateAction,
  closeAddMealDialogAction,
  deleteMealFromCachedListAction,
  initializeMealListCachingAction,
  initializeMealTypeListCachingAction,
  mealDeleteFailure,
  mealDeleteSuccess,
  mealDetailsAddFailure,
  mealDetailsAddSuccess,
  mealDetailsFetchFailure,
  mealDetailsFetchSuccess,
  mealDetailsUpdateFailure,
  mealDetailsUpdateSuccess,
  mealListFetchFailure,
  mealListFetchSuccess,
  mealListLoadRequest,
  mealTypeListFetchFailure,
  mealTypeListFetchSuccess,
  navigateToMealListViewAction,
  setMealDetailsAction,
  setMealListAction,
  setMealTypeListAction,
  updateMealInCachedListAction
} from 'app/modules/meal/store';
import {showErrorSnackBar, showLoadErrorDialog, showSuccessSnackBar} from 'app/modules/result-handling/store';
import {concatMap, map, mapTo} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class MealResultEffects {

  handleMealListFetchSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealListFetchSuccess),
      concatMap(action => [
        setMealListAction({meals: action.meals}),
        initializeMealListCachingAction({mealListToCache: action.meals})
      ])
    )
  );

  handleMealTypeListFetchSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealTypeListFetchSuccess),
      concatMap(action => [
        setMealTypeListAction({mealTypes: action.mealTypes}),
        initializeMealTypeListCachingAction({mealTypeListToCache: action.mealTypes})
      ])
    )
  );

  handleMealDetailsFetchSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealDetailsFetchSuccess),
      map(action => setMealDetailsAction({meal: action.loadedMeal}))
    )
  );

  handleMealsFetchFailure$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealListFetchFailure, mealTypeListFetchFailure, mealDetailsFetchFailure),
      map(action => showLoadErrorDialog({error: action.error}))
    )
  );

  handleMealDetailsAddSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealDetailsAddSuccess),
      concatMap(action => [
          addMealToCachedListAction({mealToAdd: action.addedMeal}),
          closeAddMealDialogAction(),
          mealListLoadRequest(),
          showSuccessSnackBar({message: `Dodano posiłek ${action.addedMeal.name}`})
        ]
      )
    )
  );

  handleMealDetailsUpdateSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealDetailsUpdateSuccess),
      concatMap(action => [
        updateMealInCachedListAction({mealToUpdate: action.updatedMeal}),
        navigateToMealListViewAction({isStateSaved: true}),
        showSuccessSnackBar({message: `Uaktualniono posiłek ${action.updatedMeal.name}`})
      ])
    )
  );

  handleMealDetailsUpdateCancel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(cancelMealDetailsUpdateAction),
      mapTo(navigateToMealListViewAction({isStateSaved: false}))
    )
  );

  handleMealDeleteSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealDeleteSuccess),
      concatMap(action => [
        deleteMealFromCachedListAction({mealIdToRemove: action.deletedMeal.id}),
        mealListLoadRequest(),
        showSuccessSnackBar({message: `Usunięto posiłek ${action.deletedMeal.name}`})
      ])
    )
  );

  handleMealsModifyingFailure$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealDetailsAddFailure, mealDetailsUpdateFailure, mealDeleteFailure),
      map(action => showErrorSnackBar({error: action.error}))
    )
  );

  constructor(private actions$: Actions) {}
}
