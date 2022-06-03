import {ActionReducerMap} from '@ngrx/store';
import {unitCacheStateReducer} from 'app/modules/unit/store/unit-cache/unit-cache.reducer';
import {unitListStateReducer} from 'app/modules/unit/store/unit-list/unit-list.reducer';
import {UnitState} from 'app/modules/unit/store/unit.state';

export const unitStateReducers: ActionReducerMap<UnitState> = {
  unitListState: unitListStateReducer,
  unitCacheState: unitCacheStateReducer
};
