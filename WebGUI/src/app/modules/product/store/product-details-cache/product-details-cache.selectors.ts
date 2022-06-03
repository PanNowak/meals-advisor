import {createSelector} from '@ngrx/store';
import {productDetailsCacheAdapter} from 'app/modules/product/store/product-details-cache/product-details-cache.state';
import {getProductState} from 'app/modules/product/store/product.selectors';

export const getProductDetailsCacheState = createSelector(getProductState,
  productState => productState.productDetailsCacheState);

export const {selectEntities: getCachedProductDetails, selectIds: getCachedProductIds} =
  productDetailsCacheAdapter.getSelectors(getProductDetailsCacheState);
