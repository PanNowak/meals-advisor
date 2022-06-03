import {createEntityAdapter, EntityState} from '@ngrx/entity';
import {comparingStrings} from 'app/modules/shared/comparers';
import {Unit} from 'app/modules/unit/models';

export const unitListAdapter =
  createEntityAdapter<Unit>({sortComparer: comparingStrings(unit => unit.name)});

export interface UnitListState extends EntityState<Unit>{}

export const initialState: UnitListState = unitListAdapter.getInitialState();
