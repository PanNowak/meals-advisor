import {createReducer, on} from '@ngrx/store';
import {Unit} from 'app/modules/unit/models';
import {invalidateUnitListAction, setCachedUnitListAction} from 'app/modules/unit/store/unit-cache/unit-cache.actions';
import {initialState, unitCacheAdapter, UnitCacheState} from 'app/modules/unit/store/unit-cache/unit-cache.state';

export const unitCacheStateReducer = createReducer(initialState,
  on(invalidateUnitListAction, () => initialState),
  on(setCachedUnitListAction, setCachedUnitList)
);

function setCachedUnitList(state: UnitCacheState,
                           action: { unitListToCache: Unit[]; }): UnitCacheState {
  return {
    ...unitCacheAdapter.setAll(action.unitListToCache, state),
    isInvalidated: false
  };
}
