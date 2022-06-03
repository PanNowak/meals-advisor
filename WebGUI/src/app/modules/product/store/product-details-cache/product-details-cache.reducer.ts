import {createReducer, on} from '@ngrx/store';
import {Product} from 'app/modules/product/models';
import {
  initialState,
  productDetailsCacheAdapter,
  ProductDetailsCacheState
} from 'app/modules/product/store/product-details-cache/product-details-cache.state';
import {
  addProductDetailsToCacheAction,
  deleteProductDetailsFromCacheAction
} from 'app/modules/product/store/product-details-cache/product-details-cache.actions';
import {CacheRecord} from 'app/modules/shared/cache.record';
import {environment} from 'environments/environment';

const maxCacheCount = environment.cache.maxNumberOfDetailEntities;

export const productDetailsCacheStateReducer = createReducer(initialState,
  on(addProductDetailsToCacheAction, addProductDetailsToCache),
  on(deleteProductDetailsFromCacheAction, deleteProductDetailsFromCache)
);

function addProductDetailsToCache(state: ProductDetailsCacheState,
                                  action: { productToCache: CacheRecord<Product>; }): ProductDetailsCacheState {
  const stateWithNewProduct = productDetailsCacheAdapter.upsertOne(action.productToCache, state);
  const oldestProductIds = extractOldestProductIds(stateWithNewProduct.ids as number[]);

  return productDetailsCacheAdapter.removeMany(oldestProductIds, stateWithNewProduct);
}

function extractOldestProductIds(productIds: number[]): number[] {
  const numberOfProductsToRemove = Math.max(0, productIds.length - maxCacheCount);
  return productIds.slice(0, numberOfProductsToRemove);
}

function deleteProductDetailsFromCache(state: ProductDetailsCacheState,
                                       action: { productIdToRemove: number; }): ProductDetailsCacheState {
  return productDetailsCacheAdapter.removeOne(action.productIdToRemove, state);
}
