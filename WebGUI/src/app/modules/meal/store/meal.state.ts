import {MealCacheState} from 'app/modules/meal/store/meal-cache/meal-cache.state';
import {MealDetailsState} from 'app/modules/meal/store/meal-details/meal-details.state';
import {MealListControlsState} from 'app/modules/meal/store/meal-list-controls/meal-list-controls.state';
import {MealListState} from 'app/modules/meal/store/meal-list/meal-list.state';
import {MealTypeCacheState} from 'app/modules/meal/store/meal-type-cache/meal-type-cache.state';
import {MealTypeListState} from 'app/modules/meal/store/meal-type-list/meal-type-list.state';

export interface MealState {
  mealCacheState: MealCacheState;
  mealListState: MealListState;
  mealDetailsState: MealDetailsState;
  mealTypeListState: MealTypeListState;
  mealTypeCacheState: MealTypeCacheState;
  mealListControlsState: MealListControlsState;
}
