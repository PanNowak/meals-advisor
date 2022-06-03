import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import {Product, ProductSummary} from 'app/modules/product/models';
import {
  addProductDetailsToCacheAction,
  deleteProductDetailsFromCacheAction,
  getCachedProductIds,
  initializeProductDetailsCaching,
  initializeProductListCachingAction,
  invalidateProductListAction,
  setCachedProductListAction
} from 'app/modules/product/store';
import {CacheInvalidationService} from 'app/modules/shared/cache-invalidation.service';
import {CacheRecord} from 'app/modules/shared/cache.record';
import {CachedIdsState} from 'app/modules/shared/cached-ids.state';
import {AppState} from 'app/store/app.state';
import {Observable, of} from 'rxjs';
import {concatMap, filter, first, groupBy, map, mergeAll, mergeMap, scan, switchMap, takeUntil} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class ProductCacheEffects { // TODO remove from deps in server mode (same for other caches)

  cacheProductList$ = createEffect(() =>
    this.actions$.pipe(
      ofType(initializeProductListCachingAction),
      switchMap(action => this.handleProductListCaching(action.productListToCache))
    )
  );

  cacheProductDetails$ = createEffect(() =>
    this.actions$.pipe(
      ofType(initializeProductDetailsCaching),
      groupBy(action => action.productToCache.id, action => action.productToCache,
        group => this.observeRemovalOfProductById(group.key)),
      mergeMap(productsToCacheById$ => productsToCacheById$.pipe(
        switchMap(productToCache => this.handleProductDetailsCaching(productToCache)),
        takeUntil(this.observeRemovalOfProductById(productsToCacheById$.key)))
      )
    )
  );

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private cacheInvalidationService: CacheInvalidationService) {}

  private handleProductListCaching(products: ProductSummary[]): Observable<Action> {
    return of(
      of(setCachedProductListAction({productListToCache: products})),
      this.cacheInvalidationService.delayedObservable(invalidateProductListAction())
    ).pipe(mergeAll());
  }

  private observeRemovalOfProductById(productId: number): Observable<number> {
    return this.store.select(getCachedProductIds).pipe(
      map(productIds => new Set(productIds as number[])),
      scan((oldIdsState, newProductIds) =>
        oldIdsState.createNewState(newProductIds), new CachedIdsState()),
      concatMap(old => old.idsToRemove),
      filter(idToRemove => productId === idToRemove),
      first()
    );
  }

  private handleProductDetailsCaching(product: Product): Observable<Action> {
    const productDetails: CacheRecord<Product> = {data: product, createdOn: new Date()};

    return of(
      of(addProductDetailsToCacheAction({productToCache: productDetails})),
      this.cacheInvalidationService.delayedObservable(deleteProductDetailsFromCacheAction({productIdToRemove: product.id}))
    ).pipe(mergeAll());
  }
}
