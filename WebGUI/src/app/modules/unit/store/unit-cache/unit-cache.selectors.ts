import {createSelector} from '@ngrx/store';
import {unitCacheAdapter} from 'app/modules/unit/store/unit-cache/unit-cache.state';
import {getUnitState} from 'app/modules/unit/store/unit.selectors';

export const getUnitCacheState = createSelector(getUnitState,
  unitState => unitState.unitCacheState);

export const {selectAll: getCachedUnitList} = unitCacheAdapter.getSelectors();
