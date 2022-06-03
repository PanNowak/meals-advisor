import {createAction, props} from '@ngrx/store';
import {Product, ProductSummary} from 'app/modules/product/models';

const actionCategory = 'Product List Cache';

export const initializeProductListCachingAction = createAction(
  `[${actionCategory}] Initialize Caching`, props<{ productListToCache: ProductSummary[]; }>()
);

export const setCachedProductListAction = createAction(
  `[${actionCategory}] Set List`, props<{ productListToCache: ProductSummary[]; }>()
);

export const addProductToCachedListAction = createAction(
  `[${actionCategory}] Add To List`, props<{ productToAdd: Product; }>()
);

export const deleteProductFromCachedListAction = createAction(
  `[${actionCategory}] Remove From List`, props<{ productIdToRemove: number; }>()
);

export const updateProductInCachedListAction = createAction(
  `[${actionCategory}] Update In List`, props<{ productToUpdate: Product; }>()
);

export const invalidateProductListAction = createAction(
  `[${actionCategory}] Invalidate List`
);
