import {createEntityAdapter, EntityState} from '@ngrx/entity';
import {MealSummary} from 'app/modules/meal/models';
import {CacheState, initialCacheState} from 'app/modules/shared/cache.state';
import {comparingStrings} from 'app/modules/shared/comparers';

export const mealCacheAdapter =
  createEntityAdapter<MealSummary>({sortComparer: comparingStrings(meal => meal.name)});

export interface MealCacheState extends EntityState<MealSummary>, CacheState {}

export const initialState: MealCacheState = mealCacheAdapter.getInitialState(initialCacheState);
