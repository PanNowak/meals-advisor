import {createReducer, on} from '@ngrx/store';
import {MealType} from 'app/modules/meal/models';
import {invalidateMealTypeListAction, setCachedMealTypeListAction} from 'app/modules/meal/store/meal-type-cache/meal-type-cache.actions';
import {initialState, mealTypeCacheAdapter, MealTypeCacheState} from 'app/modules/meal/store/meal-type-cache/meal-type-cache.state';

export const mealTypeCacheStateReducer = createReducer(initialState,
  on(invalidateMealTypeListAction, () => initialState),
  on(setCachedMealTypeListAction, setCachedMealTypeList)
);

function setCachedMealTypeList(state: MealTypeCacheState,
                               action: { mealTypeListToCache: MealType[]; }): MealTypeCacheState {
  return {
    ...mealTypeCacheAdapter.setAll(action.mealTypeListToCache, state),
    isInvalidated: false
  };
}
