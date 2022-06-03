import {Injectable} from '@angular/core';
import {GuardsCheckEnd, Router} from '@angular/router';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {navigateToHomeViewAction, navigateToPlanningViewAction} from 'app/components/app.component';
import {AppState} from 'app/store/app.state';
import {
  clearMealDetailsStateAction,
  clearMealListStateAction,
  clearMealTypeListStateAction,
  navigateToMealDetailsViewAction,
  navigateToMealListViewAction
} from 'app/modules/meal/store';
import {
  clearProductDetailsStateAction,
  clearProductListStateAction,
  navigateToProductDetailsViewAction,
  navigateToProductListViewAction
} from 'app/modules/product/store';
import {homePath, mealPath, planningPath, productPath} from 'app/modules/routing/routings';
import {clearUnitListStateAction} from 'app/modules/unit/store';
import {exhaustMap, filter, mergeMapTo} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class RoutingEffects {

  navigateToHomeView$ = createEffect(() =>
    this.actions$.pipe(
      ofType(navigateToHomeViewAction),
      exhaustMap(action => this.router.navigate([homePath], {state: action}))
    ), {dispatch: false});

  navigateToPlanningView$ = createEffect(() =>
    this.actions$.pipe(
      ofType(navigateToPlanningViewAction),
      exhaustMap(action => this.router.navigate([planningPath], {state: action}))
    ), {dispatch: false});

  navigateToMealListView$ = createEffect(() =>
    this.actions$.pipe(
      ofType(navigateToMealListViewAction),
      exhaustMap(action => this.router.navigate([mealPath], {state: action}))
    ), {dispatch: false});

  navigateToMealDetailsView$ = createEffect(() =>
    this.actions$.pipe(
      ofType(navigateToMealDetailsViewAction),
      exhaustMap(action => this.router
        .navigate([mealPath, action.requestedMealId], {state: action}))
    ), {dispatch: false});

  navigateToProductListView$ = createEffect(() =>
    this.actions$.pipe(
      ofType(navigateToProductListViewAction),
      exhaustMap(action => this.router.navigate([productPath], {state: action}))
    ), {dispatch: false});

  navigateToProductDetailsView$ = createEffect(() =>
    this.actions$.pipe(
      ofType(navigateToProductDetailsViewAction),
      exhaustMap(action => this.router
        .navigate([productPath, action.requestedProductId], {state: action}))
    ), {dispatch: false});

  clearTransientStateBeforeResolving$ = createEffect(() =>
    this.router.events.pipe(
      filter(event => event instanceof GuardsCheckEnd && event.shouldActivate),
      mergeMapTo([
        clearMealListStateAction(),
        clearMealDetailsStateAction(),
        clearMealTypeListStateAction(),
        clearProductListStateAction(),
        clearProductDetailsStateAction(),
        clearUnitListStateAction()
      ])
    )
  );

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private router: Router) {}
}
