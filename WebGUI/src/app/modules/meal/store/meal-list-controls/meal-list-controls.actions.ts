import {createAction, props} from '@ngrx/store';

const actionCategory = 'Meal List Controls';

export const setMealSearchValueAction = createAction(
  `[${actionCategory}] Set Search Value`, props<{ searchValue: string; }>()
);

export const setMealPaginationInfoAction = createAction(
  `[${actionCategory}] Set Pagination Info`, props<{ pageIndex: number; pageSize: number; }>()
);
