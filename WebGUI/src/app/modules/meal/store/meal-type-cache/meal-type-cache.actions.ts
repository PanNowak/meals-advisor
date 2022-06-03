import {createAction, props} from '@ngrx/store';
import {MealType} from 'app/modules/meal/models';

const actionCategory = 'Meal Type Cache';

export const initializeMealTypeListCachingAction = createAction(
  `[${actionCategory}] Initialize List Caching`, props<{ mealTypeListToCache: MealType[]; }>()
);

export const setCachedMealTypeListAction = createAction(
  `[${actionCategory}] Set List`, props<{ mealTypeListToCache: MealType[]; }>()
);

export const invalidateMealTypeListAction = createAction(
  `[${actionCategory}] Invalidate List`
);
