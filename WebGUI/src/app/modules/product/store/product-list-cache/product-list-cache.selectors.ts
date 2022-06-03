import {createSelector} from '@ngrx/store';
import {productListCacheAdapter} from 'app/modules/product/store/product-list-cache/product-list-cache.state';
import {getProductState} from 'app/modules/product/store/product.selectors';

export const getProductListCacheState = createSelector(getProductState,
  productState => productState.productListCacheState);

export const {selectAll: getCachedProductList} = productListCacheAdapter.getSelectors();
