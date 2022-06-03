import {ProductDetailsCacheState} from 'app/modules/product/store/product-details-cache/product-details-cache.state';
import {ProductListCacheState} from 'app/modules/product/store/product-list-cache/product-list-cache.state';
import {ProductDetailsState} from 'app/modules/product/store/product-details/product-details.state';
import {ProductListControlsState} from 'app/modules/product/store/product-list-controls/product-list-controls.state';
import {ProductListState} from 'app/modules/product/store/product-list/product-list.state';

export interface ProductState {
  productListState: ProductListState;
  productDetailsState: ProductDetailsState;
  productListCacheState: ProductListCacheState;
  productDetailsCacheState: ProductDetailsCacheState;
  productListControlsState: ProductListControlsState;
}
