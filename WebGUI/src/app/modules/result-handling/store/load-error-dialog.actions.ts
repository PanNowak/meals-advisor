import {createAction, props} from '@ngrx/store';

const actionCategory = 'Load Error Dialog';

export const showLoadErrorDialog = createAction(
  `[${actionCategory}] Show`, props<{ error: Error; }>()
);
