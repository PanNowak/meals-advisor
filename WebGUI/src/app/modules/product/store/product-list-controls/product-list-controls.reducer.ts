import {createReducer, on} from '@ngrx/store';
import {
  setProductPaginationInfoAction,
  setProductSearchValueAction
} from 'app/modules/product/store/product-list-controls/product-list-controls.actions';
import {initialState, ProductListControlsState} from 'app/modules/product/store/product-list-controls/product-list-controls.state';

export const productListControlsStateReducer = createReducer(initialState,
  on(setProductSearchValueAction, setProductSearchValue),
  on(setProductPaginationInfoAction, setProductPaginationInfo)
);

function setProductSearchValue(state: ProductListControlsState,
                               action: { searchValue: string; }): ProductListControlsState {
  return {
    ...state,
    searchValue: action.searchValue
  };
}

function setProductPaginationInfo(state: ProductListControlsState,
                                  action: { pageIndex: number; pageSize: number; }): ProductListControlsState {
  return {
    ...state,
    pageIndex: action.pageIndex,
    pageSize: action.pageSize
  };
}
