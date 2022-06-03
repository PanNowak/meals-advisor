import {Injectable} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {ProductAddComponent, SecondaryUnitAddDialogComponent} from 'app/modules/product/components';
import {ProductSummary} from 'app/modules/product/models';
import {
  addSecondaryUnitAction,
  cancelProductDetailsAddAction,
  clearProductDetailsStateAction,
  closeAddProductDialogAction,
  getAvailableSecondaryUnits, isCurrentProductIdenticalToLoaded,
  openAddProductDialogAction,
  openAddSecondaryUnitDialogAction,
  openDeleteProductDialogAction,
  productDeleteRequest
} from 'app/modules/product/store';
import {ConfirmationDialogComponent} from 'app/modules/shared/confirmation-dialog/confirmation-dialog.component';
import {DialogFactory} from 'app/modules/shared/dialog.factory';
import {clearUnitListStateAction, unitListLoadRequest} from 'app/modules/unit/store';
import {isNotNullOrUndefined} from 'codelyzer/util/isNotNullOrUndefined';
import {identity, Observable, ObservableInput} from 'rxjs';
import {concatMap, defaultIfEmpty, exhaustMap, filter, first, map, mapTo} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class ProductDialogEffects {

  openAddProductDialog$ = createEffect(() =>
    this.actions$.pipe(
      ofType(openAddProductDialogAction),
      exhaustMap(() => this.handleAddProductDialogOpening())
    ), {dispatch: false}
  );

  handleProductDetailsAddCancel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(cancelProductDetailsAddAction),
      exhaustMap(() => this.confirmAddProductDialogClosing()),
      filter(identity),
      mapTo(closeAddProductDialogAction())
    )
  );

  closeAddProductDialog$ = createEffect(() =>
    this.actions$.pipe(
      ofType(closeAddProductDialogAction),
      concatMap(() => this.handleAddProductDialogClosing())
    )
  );

  openDeleteProductDialog$ = createEffect(() =>
    this.actions$.pipe(
      ofType(openDeleteProductDialogAction),
      exhaustMap(action => this.handleProductDeleteConfirmation(action.productToDelete))
    )
  );

  openAddSecondaryUnitDialog$ = createEffect(() =>
    this.actions$.pipe(
      ofType(openAddSecondaryUnitDialogAction),
      exhaustMap(() => this.dialog.open(SecondaryUnitAddDialogComponent,
        {minWidth: '50%', data: this.store.select(getAvailableSecondaryUnits)})
        .afterClosed()),
      filter(isNotNullOrUndefined),
      map(secondaryUnit => addSecondaryUnitAction({secondaryUnitToAdd: secondaryUnit}))
    )
  );

  private addProductDialogRef: MatDialogRef<ProductAddComponent>;

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private dialog: MatDialog,
              private dialogFactory: DialogFactory) {}

  private handleAddProductDialogOpening(): Observable<any> {
    this.store.dispatch(unitListLoadRequest());

    this.addProductDialogRef = this.dialog.open(ProductAddComponent, {width: '100%', disableClose: true});
    return this.addProductDialogRef.afterClosed();
  }

  private confirmAddProductDialogClosing(): Observable<boolean> {
    return this.store.select(isCurrentProductIdenticalToLoaded)
      .pipe(
        first(),
        filter(isUnchanged => !isUnchanged),
        concatMap(() => this.dialogFactory.showConfirmationDialog(
          `Czy jesteś pewien, że chcesz zamknąć okno? Stracisz niezapisane zmiany`)),
        defaultIfEmpty(true)
      );
  }

  private handleAddProductDialogClosing(): ObservableInput<Action> {
    if (this.addProductDialogRef) {
      this.addProductDialogRef.close();
      this.addProductDialogRef = null;
    }

    return [clearProductDetailsStateAction(), clearUnitListStateAction()];
  }

  private handleProductDeleteConfirmation(product: ProductSummary): Observable<Action> {
    return this.dialog.open(ConfirmationDialogComponent, {
      data: `Czy jesteś pewien, że chcesz usunąć produkt ${product.name}?`
    })
      .afterClosed()
      .pipe(
        filter(r => r),
        mapTo(productDeleteRequest({productToDelete: product}))
      );
  }
}
