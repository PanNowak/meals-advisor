import {createAction, props} from '@ngrx/store';
import {Unit} from 'app/modules/unit/models';

const actionCategory = 'Unit Cache';

export const initializeUnitListCachingAction = createAction(
  `[${actionCategory}] Initialize List Caching`, props<{ unitListToCache: Unit[]; }>()
);

export const setCachedUnitListAction = createAction(
  `[${actionCategory}] Set List`, props<{ unitListToCache: Unit[]; }>()
);

export const invalidateUnitListAction = createAction(
  `[${actionCategory}] Invalidate List`
);
