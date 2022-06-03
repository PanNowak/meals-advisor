import {createAction, props} from '@ngrx/store';

const actionCategory = 'Product List Controls';

export const setProductSearchValueAction = createAction(
  `[${actionCategory}] Set Search Value`, props<{ searchValue: string; }>()
);

export const setProductPaginationInfoAction = createAction(
  `[${actionCategory}] Set Pagination Info`, props<{ pageIndex: number; pageSize: number; }>()
);
