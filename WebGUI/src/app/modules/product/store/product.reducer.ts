
import {ActionReducerMap} from '@ngrx/store';
import {productDetailsCacheStateReducer} from 'app/modules/product/store/product-details-cache/product-details-cache.reducer';
import {productDetailsStateReducer} from 'app/modules/product/store/product-details/product-details.reducer';
import {productListCacheStateReducer} from 'app/modules/product/store/product-list-cache/product-list-cache.reducer';
import {productListControlsStateReducer} from 'app/modules/product/store/product-list-controls/product-list-controls.reducer';
import {productListStateReducer} from 'app/modules/product/store/product-list/product-list.reducer';
import {ProductState} from 'app/modules/product/store/product.state';

export const productStateReducers: ActionReducerMap<ProductState> = {
  productListState: productListStateReducer,
  productDetailsState: productDetailsStateReducer,
  productListCacheState: productListCacheStateReducer,
  productDetailsCacheState: productDetailsCacheStateReducer,
  productListControlsState: productListControlsStateReducer
};
