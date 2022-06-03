import {createSelector} from '@ngrx/store';
import {unitListAdapter} from 'app/modules/unit/store/unit-list/unit-list.state';
import {getUnitState} from 'app/modules/unit/store/unit.selectors';

export const getUnitListState = createSelector(getUnitState, unitState => unitState.unitListState);

export const {selectAll: getUnitList} = unitListAdapter.getSelectors(getUnitListState);
