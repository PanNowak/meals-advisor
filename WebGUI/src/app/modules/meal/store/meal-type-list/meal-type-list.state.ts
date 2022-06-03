import {createEntityAdapter, EntityState} from '@ngrx/entity';
import {MealType} from 'app/modules/meal/models';
import {comparingNumbers} from 'app/modules/shared/comparers';

export const mealTypeListAdapter =
  createEntityAdapter<MealType>({sortComparer: comparingNumbers(type => type.id)});

export interface MealTypeListState extends EntityState<MealType> {}

export const initialState: MealTypeListState = mealTypeListAdapter.getInitialState();
