import {createEntityAdapter, EntityState} from '@ngrx/entity';
import {MealType} from 'app/modules/meal/models';
import {CacheState, initialCacheState} from 'app/modules/shared/cache.state';
import {comparingNumbers} from 'app/modules/shared/comparers';

export const mealTypeCacheAdapter =
  createEntityAdapter<MealType>({sortComparer: comparingNumbers(mealType => mealType.id)});

export interface MealTypeCacheState extends EntityState<MealType>, CacheState {}

export const initialState: MealTypeCacheState = mealTypeCacheAdapter.getInitialState(initialCacheState);
