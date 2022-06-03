import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {Product, ProductSummary} from 'app/modules/product/models';
import {ProductService} from 'app/modules/product/services/product.service';
import {
  getCachedProductDetails,
  getCachedProductList,
  getProductListCacheState,
  productDeleteFailure,
  productDeleteRequest,
  productDeleteSuccess,
  productDetailsAddFailure,
  productDetailsAddRequest,
  productDetailsAddSuccess,
  productDetailsFetchFailure,
  productDetailsFetchRequest,
  productDetailsFetchSuccess,
  productDetailsLoadRequest,
  productDetailsUpdateFailure,
  productDetailsUpdateRequest,
  productDetailsUpdateSuccess,
  productListFetchFailure,
  productListFetchRequest,
  productListFetchSuccess,
  productListLoadRequest,
  setProductDetailsAction,
  setProductListAction
} from 'app/modules/product/store';
import {isNotNullOrUndefined} from 'codelyzer/util/isNotNullOrUndefined';
import {Observable, of} from 'rxjs';
import {catchError, defaultIfEmpty, exhaustMap, filter, first, map, mapTo, switchMap} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class ProductCrudEffects {

  loadProductList$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productListLoadRequest),
      switchMap(() => this.handleProductListLoad())
    )
  );

  fetchProductList$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productListFetchRequest),
      switchMap(() => this.handleProductListFetch())
    )
  );

  loadProductDetails$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productDetailsLoadRequest),
      switchMap(action => this.handleProductDetailsLoad(action.productId))
    )
  );

  fetchProductDetails$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productDetailsFetchRequest),
      switchMap(action => this.handleProductDetailsFetch(action.productId))
    )
  );

  addProduct$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productDetailsAddRequest),
      exhaustMap(action => this.handleProductAdd(action.productToAdd))
    )
  );

  updateProduct$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productDetailsUpdateRequest),
      exhaustMap(action => this.handleProductUpdate(action.productToUpdate))
    )
  );

  deleteProduct$ = createEffect(() =>
    this.actions$.pipe(
      ofType(productDeleteRequest),
      exhaustMap(action => this.handleProductDeletion(action.productToDelete))
    )
  );

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private productService: ProductService) {}

  private handleProductListLoad(): Observable<Action> {
    return this.store.select(getProductListCacheState).pipe(
      first(),
      map(productCacheState => {
        if (productCacheState.isInvalidated) {
          return productListFetchRequest();
        } else {
          const cachedProductList = getCachedProductList(productCacheState);
          return setProductListAction({products: cachedProductList});
        }
      })
    );
  }

  private handleProductListFetch(): Observable<Action> {
    return this.productService.getAll()
      .pipe(
        map(productList => productListFetchSuccess({products: productList})),
        catchError(err => of(productListFetchFailure({error: err})))
      );
  }

  private handleProductDetailsLoad(productIdToLoad: number): Observable<Action> {
    return this.store.select(getCachedProductDetails).pipe(
      first(),
      map(products => products[productIdToLoad]),
      filter(isNotNullOrUndefined),
      map(productDetails => setProductDetailsAction({product: productDetails.data})),
      defaultIfEmpty<Action>(productDetailsFetchRequest({productId: productIdToLoad}))
    );
  }

  private handleProductDetailsFetch(productId: number): Observable<Action> {
    return this.productService.getById(productId)
      .pipe(
        map(product => productDetailsFetchSuccess({loadedProduct: product})),
        catchError(err => of(productDetailsFetchFailure({error: err})))
      );
  }

  private handleProductAdd(productToAdd: Product): Observable<Action> {
    return this.productService.create(productToAdd)
      .pipe(
        map(product => productDetailsAddSuccess({addedProduct: product})),
        catchError(err => of(productDetailsAddFailure({error: err})))
      );
  }

  private handleProductUpdate(productToUpdate: Product): Observable<Action> {
    return this.productService.update(productToUpdate.id, productToUpdate)
      .pipe(
        map(product => productDetailsUpdateSuccess({updatedProduct: product})),
        catchError(err => of(productDetailsUpdateFailure({error: err})))
      );
  }

  private handleProductDeletion(product: ProductSummary): Observable<Action> {
    return this.productService.deleteById(product.id)
      .pipe(
        mapTo(productDeleteSuccess({deletedProduct: product})),
        catchError(err => of(productDeleteFailure({error: err})))
      );
  }
}
