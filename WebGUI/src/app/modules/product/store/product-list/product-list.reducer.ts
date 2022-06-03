import {createReducer, on} from '@ngrx/store';
import {ProductSummary} from 'app/modules/product/models';
import {clearProductListStateAction, setProductListAction} from 'app/modules/product/store/product-list/product-list.actions';
import {initialState, productListAdapter, ProductListState} from 'app/modules/product/store/product-list/product-list.state';

export const productListStateReducer = createReducer(initialState,
  on(clearProductListStateAction, () => initialState),
  on(setProductListAction, setProductList)
);

function setProductList(state: ProductListState,
                        action: { products: ProductSummary[]; }): ProductListState {
  return productListAdapter.setAll(action.products, state);
}
