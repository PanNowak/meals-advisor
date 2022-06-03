import {createSelector} from '@ngrx/store';
import {productListAdapter} from 'app/modules/product/store/product-list/product-list.state';
import {getProductState} from 'app/modules/product/store/product.selectors';

export const getProductListState = createSelector(getProductState,
  productState => productState.productListState);

export const {selectAll: getProductList} = productListAdapter.getSelectors(getProductListState);

export const getAllProductNames = createSelector(getProductList,
  productList => productList.map(product => product.name));
