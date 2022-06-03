import {Injectable} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {IngredientAddDialogComponent, MealAddComponent} from 'app/modules/meal/components';
import {MealSummary} from 'app/modules/meal/models';
import {
  addIngredientAction,
  cancelMealDetailsAddAction,
  clearMealDetailsStateAction,
  clearMealTypeListStateAction,
  closeAddMealDialogAction,
  isCurrentMealIdenticalToLoaded,
  mealDeleteRequest,
  mealTypeListLoadRequest,
  openAddIngredientDialogAction,
  openAddMealDialogAction,
  openDeleteMealDialogAction
} from 'app/modules/meal/store';
import {clearProductDetailsStateAction, clearProductListStateAction, productListLoadRequest} from 'app/modules/product/store';
import {ConfirmationDialogComponent} from 'app/modules/shared/confirmation-dialog/confirmation-dialog.component';
import {DialogFactory} from 'app/modules/shared/dialog.factory';
import {identity, Observable, ObservableInput} from 'rxjs';
import {concatMap, defaultIfEmpty, exhaustMap, filter, first, mapTo} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class MealDialogEffects {

  openAddMealDialog$ = createEffect(() =>
    this.actions$.pipe(
      ofType(openAddMealDialogAction),
      exhaustMap(() => this.handleAddMealDialogOpening())
    ), {dispatch: false}
  );

  handleMealDetailsAddCancel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(cancelMealDetailsAddAction),
      exhaustMap(() => this.confirmAddMealDialogClosing()),
      filter(identity),
      mapTo(closeAddMealDialogAction())
    )
  );

  closeAddMealDialog$ = createEffect(() =>
    this.actions$.pipe(
      ofType(closeAddMealDialogAction),
      concatMap(() => this.handleAddMealDialogClosing())
    )
  );

  openDeleteMealDialog$ = createEffect(() =>
    this.actions$.pipe(
      ofType(openDeleteMealDialogAction),
      exhaustMap(action => this.handleMealDeleteConfirmation(action.mealToDelete))
    )
  );

  openAddIngredientDialog$ = createEffect(() =>
    this.actions$.pipe(
      ofType(openAddIngredientDialogAction),
      exhaustMap(() => this.dialog.open(IngredientAddDialogComponent, {minWidth: '50%'})
        .afterClosed()),
      concatMap(ingredient => ingredient ?
        [addIngredientAction({ingredientToAdd: ingredient}), clearProductDetailsStateAction()] :
        [clearProductDetailsStateAction()]
      )
    )
  );

  private addMealDialogRef: MatDialogRef<MealAddComponent>;

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private dialog: MatDialog,
              private dialogFactory: DialogFactory) {}

  private handleAddMealDialogOpening(): Observable<any> {
    this.store.dispatch(mealTypeListLoadRequest());
    this.store.dispatch(productListLoadRequest());

    this.addMealDialogRef = this.dialog.open(MealAddComponent, {width: '100%', disableClose: true});
    return this.addMealDialogRef.afterClosed();
  }

  private confirmAddMealDialogClosing(): Observable<boolean> {
    return this.store.select(isCurrentMealIdenticalToLoaded)
      .pipe(
        first(),
        filter(isUnchanged => !isUnchanged),
        concatMap(() => this.dialogFactory.showConfirmationDialog(
          `Czy jesteś pewien, że chcesz zamknąć okno? Stracisz niezapisane zmiany`)),
        defaultIfEmpty(true)
      );
  }

  private handleAddMealDialogClosing(): ObservableInput<Action> {
    if (this.addMealDialogRef) {
      this.addMealDialogRef.close();
      this.addMealDialogRef = null;
    }

    return [clearMealDetailsStateAction(), clearMealTypeListStateAction(), clearProductListStateAction()];
  }

  private handleMealDeleteConfirmation(meal: MealSummary): Observable<Action> {
    return this.dialog.open(ConfirmationDialogComponent, {
      data: `Czy jesteś pewien, że chcesz usunąć posiłek ${meal.name}?`
    })
      .afterClosed()
      .pipe(
        filter(r => r),
        mapTo(mealDeleteRequest({mealToDelete: meal}))
      );
  }
}
