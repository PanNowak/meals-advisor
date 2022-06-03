import {createAction} from '@ngrx/store';

const actionCategory = 'Background Task Count';

export const incrementBackgroundTaskCount =
  createAction(`[${actionCategory}] Increment`);
export const decrementBackgroundTaskCount =
  createAction(`[${actionCategory}] Decrement`);
