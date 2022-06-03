import {createSelector} from '@ngrx/store';
import {mealCacheAdapter} from 'app/modules/meal/store/meal-cache/meal-cache.state';
import {getMealState} from 'app/modules/meal/store/meal.selectors';

export const getMealCacheState = createSelector(getMealState,
  mealState => mealState.mealCacheState);

export const {selectAll: getCachedMealList} = mealCacheAdapter.getSelectors();
