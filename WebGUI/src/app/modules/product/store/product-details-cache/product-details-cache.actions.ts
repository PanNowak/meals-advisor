import {createAction, props} from '@ngrx/store';
import {Product} from 'app/modules/product/models';
import {CacheRecord} from 'app/modules/shared/cache.record';

const actionCategory = 'Product Details Cache';

export const initializeProductDetailsCaching = createAction(
  `[${actionCategory}] Initialize Caching`, props<{ productToCache: Product; }>()
);

export const addProductDetailsToCacheAction = createAction(
  `[${actionCategory}] Add Product`, props<{ productToCache: CacheRecord<Product>; }>()
);

export const deleteProductDetailsFromCacheAction = createAction(
  `[${actionCategory}] Delete Product`, props<{ productIdToRemove: number; }>()
);
