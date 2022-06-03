import {createAction, props} from '@ngrx/store';
import {Unit} from 'app/modules/unit/models';

const actionCategory = 'Unit List';

export const unitListLoadRequest = createAction(
  `[${actionCategory}] Load Request`
);

export const unitListFetchRequest = createAction(
  `[${actionCategory}] Fetch Request`
);

export const unitListFetchSuccess = createAction(
  `[${actionCategory}] Fetch Success`, props<{ units: Unit[]; }>()
);

export const unitListFetchFailure = createAction(
  `[${actionCategory}] Load Failure`, props<{ error: Error; }>()
);

export const setUnitListAction = createAction(
  `[${actionCategory}] Set List`, props<{ units: Unit[]; }>()
);

export const clearUnitListStateAction = createAction(
  `[${actionCategory}] Clear State`
);
