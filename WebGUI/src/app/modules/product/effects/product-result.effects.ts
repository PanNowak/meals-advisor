import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {
  addProductToCachedListAction,
  cancelProductDetailsUpdateAction,
  closeAddProductDialogAction,
  deleteProductDetailsFromCacheAction,
  deleteProductFromCachedListAction,
  initializeProductDetailsCaching,
  initializeProductListCachingAction,
  navigateToProductListViewAction,
  productDeleteFailure,
  productDeleteSuccess,
  productDetailsAddFailure,
  productDetailsAddSuccess,
  productDetailsFetchFailure,
  productDetailsFetchSuccess,
  productDetailsUpdateFailure,
  productDetailsUpdateSuccess,
  productListFetchFailure,
  productListFetchSuccess,
  productListLoadRequest,
  setProductDetailsAction,
  setProductListAction,
  updateProductInCachedListAction
} from 'app/modules/product/store';
import {showErrorSnackBar, showLoadErrorDialog, showSuccessSnackBar} from 'app/modules/result-handling/store';
import {concatMap, map, mapTo} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class ProductResultEffects {

  handleProductListFetchSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productListFetchSuccess),
      concatMap(action => [
        setProductListAction({products: action.products}),
        initializeProductListCachingAction({productListToCache: action.products})
      ])
    )
  );

  handleProductDetailsFetchSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productDetailsFetchSuccess),
      concatMap(action => [
        setProductDetailsAction({product: action.loadedProduct}),
        initializeProductDetailsCaching({productToCache: action.loadedProduct})
      ])
    )
  );

  handleProductsFetchFailure$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productListFetchFailure, productDetailsFetchFailure),
      map(action => showLoadErrorDialog({error: action.error}))
    )
  );

  handleProductDetailsAddSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productDetailsAddSuccess),
      concatMap(action => [
          addProductToCachedListAction({productToAdd: action.addedProduct}),
          initializeProductDetailsCaching({productToCache: action.addedProduct}),
          closeAddProductDialogAction(),
          productListLoadRequest(),
          showSuccessSnackBar({message: `Dodano produkt ${action.addedProduct.name}`})
        ]
      )
    )
  );

  handleProductDetailsUpdateSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productDetailsUpdateSuccess),
      concatMap(action => [
        updateProductInCachedListAction({productToUpdate: action.updatedProduct}),
        initializeProductDetailsCaching({productToCache: action.updatedProduct}),
        navigateToProductListViewAction({isStateSaved: true}),
        showSuccessSnackBar({message: `Uaktualniono produkt ${action.updatedProduct.name}`})
      ])
    )
  );

  handleProductDetailsUpdateCancel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(cancelProductDetailsUpdateAction),
      mapTo(navigateToProductListViewAction({isStateSaved: false}))
    )
  );

  handleProductDeleteSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productDeleteSuccess),
      concatMap(action => [
        deleteProductFromCachedListAction({productIdToRemove: action.deletedProduct.id}),
        deleteProductDetailsFromCacheAction({productIdToRemove: action.deletedProduct.id}),
        productListLoadRequest(),
        showSuccessSnackBar({message: `Usunięto produkt ${action.deletedProduct.name}`})
      ])
    )
  );

  handleProductsModifyingFailure$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productDetailsAddFailure, productDetailsUpdateFailure, productDeleteFailure),
      map(action => showErrorSnackBar({error: action.error}))
    )
  );

  constructor(private actions$: Actions) {}
}
