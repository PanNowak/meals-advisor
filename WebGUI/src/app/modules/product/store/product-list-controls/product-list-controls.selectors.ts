import {createSelector} from '@ngrx/store';
import {getProductState} from 'app/modules/product/store/product.selectors';

export const getProductListControlsState = createSelector(getProductState,
  productState => productState.productListControlsState);
