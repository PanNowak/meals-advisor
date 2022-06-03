import {createReducer, on} from '@ngrx/store';
import {Unit} from 'app/modules/unit/models';
import {clearUnitListStateAction, setUnitListAction} from 'app/modules/unit/store/unit-list/unit-list.actions';
import {initialState, unitListAdapter, UnitListState} from 'app/modules/unit/store/unit-list/unit-list.state';

export const unitListStateReducer = createReducer(initialState,
  on(clearUnitListStateAction, () => initialState),
  on(setUnitListAction, setUnitList)
);

function setUnitList(currentState: UnitListState, action: { units: Unit[] }): UnitListState {
  return unitListAdapter.setAll(action.units, currentState);
}
