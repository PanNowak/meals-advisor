import {createEntityAdapter, EntityState} from '@ngrx/entity';
import {MealSummary} from 'app/modules/meal/models';
import {comparingStrings} from 'app/modules/shared/comparers';

export const mealListAdapter =
  createEntityAdapter<MealSummary>({sortComparer: comparingStrings(meal => meal.name)});

export interface MealListState extends EntityState<MealSummary> {}

export const initialState: MealListState = mealListAdapter.getInitialState();
