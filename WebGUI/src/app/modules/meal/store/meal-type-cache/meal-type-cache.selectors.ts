import {createSelector} from '@ngrx/store';
import {mealTypeCacheAdapter} from 'app/modules/meal/store/meal-type-cache/meal-type-cache.state';
import {getMealState} from 'app/modules/meal/store/meal.selectors';

export const getMealTypeCacheState = createSelector(getMealState,
  mealState => mealState.mealTypeCacheState);

export const {selectAll: getCachedMealTypeList} = mealTypeCacheAdapter.getSelectors();
