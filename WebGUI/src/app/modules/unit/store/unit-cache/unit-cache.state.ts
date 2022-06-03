import {createEntityAdapter, EntityState} from '@ngrx/entity';
import {CacheState, initialCacheState} from 'app/modules/shared/cache.state';
import {comparingStrings} from 'app/modules/shared/comparers';
import {Unit} from 'app/modules/unit/models';

export const unitCacheAdapter =
  createEntityAdapter<Unit>({sortComparer: comparingStrings(unit => unit.name)});

export interface UnitCacheState extends EntityState<Unit>, CacheState {}

export const initialState: UnitCacheState = unitCacheAdapter.getInitialState(initialCacheState);
