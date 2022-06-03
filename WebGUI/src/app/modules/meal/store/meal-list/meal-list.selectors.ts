import {createSelector} from '@ngrx/store';
import {mealListAdapter} from 'app/modules/meal/store/meal-list/meal-list.state';
import {getMealState} from 'app/modules/meal/store/meal.selectors';

export const getMealListState = createSelector(getMealState,
  mealState => mealState.mealListState);

export const {selectAll: getMealList} = mealListAdapter.getSelectors(getMealListState);
