import {createAction, props} from '@ngrx/store';
import {Meal, MealSummary} from 'app/modules/meal/models';

const actionCategory = 'Meal Cache';

export const initializeMealListCachingAction = createAction(
  `[${actionCategory}] Initialize List Caching`, props<{ mealListToCache: MealSummary[]; }>()
);

export const setCachedMealListAction = createAction(
  `[${actionCategory}] Set List`, props<{ mealListToCache: MealSummary[]; }>()
);

export const addMealToCachedListAction = createAction(
  `[${actionCategory}] Add To List`, props<{ mealToAdd: Meal; }>()
);

export const deleteMealFromCachedListAction = createAction(
  `[${actionCategory}] Remove From List`, props<{ mealIdToRemove: number; }>()
);

export const updateMealInCachedListAction = createAction(
  `[${actionCategory}] Update In List`, props<{ mealToUpdate: Meal; }>()
);

export const invalidateMealListAction = createAction(
  `[${actionCategory}] Invalidate List`
);
