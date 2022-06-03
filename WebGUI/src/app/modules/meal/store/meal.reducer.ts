import {ActionReducerMap} from '@ngrx/store';
import {mealCacheStateReducer} from 'app/modules/meal/store/meal-cache/meal-cache.reducer';
import {mealDetailsStateReducer} from 'app/modules/meal/store/meal-details/meal-details.reducer';
import {mealListControlsStateReducer} from 'app/modules/meal/store/meal-list-controls/meal-list-controls.reducer';
import {mealListStateReducer} from 'app/modules/meal/store/meal-list/meal-list.reducer';
import {mealTypeCacheStateReducer} from 'app/modules/meal/store/meal-type-cache/meal-type-cache.reducer';
import {mealTypeListStateReducer} from 'app/modules/meal/store/meal-type-list/meal-type-list.reducer';
import {MealState} from 'app/modules/meal/store/meal.state';

export const mealStateReducers: ActionReducerMap<MealState> = {
  mealListState: mealListStateReducer,
  mealDetailsState: mealDetailsStateReducer,
  mealCacheState: mealCacheStateReducer,
  mealTypeListState: mealTypeListStateReducer,
  mealTypeCacheState: mealTypeCacheStateReducer,
  mealListControlsState: mealListControlsStateReducer
};
