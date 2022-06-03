import {createReducer, on} from '@ngrx/store';
import {Meal, MealSummary} from 'app/modules/meal/models';
import {
  addMealToCachedListAction,
  deleteMealFromCachedListAction,
  invalidateMealListAction,
  setCachedMealListAction,
  updateMealInCachedListAction
} from 'app/modules/meal/store/meal-cache/meal-cache.actions';
import {initialState, mealCacheAdapter, MealCacheState} from 'app/modules/meal/store/meal-cache/meal-cache.state';

export const mealCacheStateReducer = createReducer(initialState,
  on(invalidateMealListAction, () => initialState),
  on(setCachedMealListAction, setCachedMealList),
  on(addMealToCachedListAction, addMealToCachedList),
  on(deleteMealFromCachedListAction, deleteMealFromCachedList),
  on(updateMealInCachedListAction, updateMealInCacheList)
);

function setCachedMealList(state: MealCacheState,
                           action: { mealListToCache: MealSummary[]; }): MealCacheState {
  return {
    ...mealCacheAdapter.setAll(action.mealListToCache, state),
    isInvalidated: false
  };
}

function addMealToCachedList(state: MealCacheState,
                             action: { mealToAdd: Meal; }): MealCacheState {
  if (state.isInvalidated) {
    return state;
  }

  return mealCacheAdapter.addOne(action.mealToAdd, state);
}

function deleteMealFromCachedList(state: MealCacheState,
                                  action: { mealIdToRemove: number; }): MealCacheState {
  if (state.isInvalidated) {
    return state;
  }

  return mealCacheAdapter.removeOne(action.mealIdToRemove, state);
}

function updateMealInCacheList(state: MealCacheState,
                               action: { mealToUpdate: Meal; }): MealCacheState {
  if (state.isInvalidated) {
    return state;
  }

  return mealCacheAdapter.upsertOne(action.mealToUpdate, state);
}
