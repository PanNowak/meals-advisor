import {createSelector} from '@ngrx/store';
import {getMealState} from 'app/modules/meal/store/meal.selectors';

export const getMealListControlsState = createSelector(getMealState,
  mealState => mealState.mealListControlsState);
