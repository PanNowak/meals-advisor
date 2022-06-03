import {createReducer, on} from '@ngrx/store';
import {Product, ProductSummary} from 'app/modules/product/models';
import {
  addProductToCachedListAction,
  deleteProductFromCachedListAction,
  invalidateProductListAction,
  setCachedProductListAction,
  updateProductInCachedListAction
} from 'app/modules/product/store/product-list-cache/product-list-cache.actions';
import {initialState, productListCacheAdapter, ProductListCacheState} from 'app/modules/product/store/product-list-cache/product-list-cache.state';

export const productListCacheStateReducer = createReducer(initialState,
  on(invalidateProductListAction, () => initialState),
  on(setCachedProductListAction, setCachedProductList),
  on(addProductToCachedListAction, addProductToCachedList),
  on(deleteProductFromCachedListAction, deleteProductFromCachedList),
  on(updateProductInCachedListAction, updateProductInCacheList)
);

function setCachedProductList(state: ProductListCacheState,
                              action: { productListToCache: ProductSummary[]; }): ProductListCacheState {
  return {
    ...productListCacheAdapter.setAll(action.productListToCache, state),
    isInvalidated: false
  };
}

function addProductToCachedList(state: ProductListCacheState,
                                action: { productToAdd: Product; }): ProductListCacheState {
  if (state.isInvalidated) {
    return state;
  }

  return productListCacheAdapter.addOne(action.productToAdd, state);
}

function deleteProductFromCachedList(state: ProductListCacheState,
                                     action: { productIdToRemove: number; }): ProductListCacheState {
  if (state.isInvalidated) {
    return state;
  }

  return productListCacheAdapter.removeOne(action.productIdToRemove, state);
}

function updateProductInCacheList(state: ProductListCacheState,
                                  action: { productToUpdate: Product; }): ProductListCacheState {
  if (state.isInvalidated) {
    return state;
  }

  return productListCacheAdapter.upsertOne(action.productToUpdate, state);
}
