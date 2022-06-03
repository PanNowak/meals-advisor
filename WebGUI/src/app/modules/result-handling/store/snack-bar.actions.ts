import {createAction, props} from '@ngrx/store';

const actionCategory = 'Snack Bar';

export const showSuccessSnackBar = createAction(
  `[${actionCategory}] Show Success`, props<{ message: string; }>()
);

export const showErrorSnackBar = createAction(
  `[${actionCategory}] Show Error`, props<{ error: Error; }>()
);
