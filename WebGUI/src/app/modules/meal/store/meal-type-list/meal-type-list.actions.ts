import {createAction, props} from '@ngrx/store';
import {MealType} from 'app/modules/meal/models';

const actionCategory = 'Meal Type List';

export const mealTypeListLoadRequest = createAction(
  `[${actionCategory}] Load Request`
);

export const mealTypeListFetchRequest = createAction(
  `[${actionCategory}] Fetch Request`
);

export const mealTypeListFetchSuccess = createAction(
  `[${actionCategory}] Fetch Success`, props<{ mealTypes: MealType[]; }>()
);

export const mealTypeListFetchFailure = createAction(
  `[${actionCategory}] Load Failure`, props<{ error: Error; }>()
);

export const setMealTypeListAction = createAction(
  `[${actionCategory}] Set List`, props<{ mealTypes: MealType[]; }>()
);

export const clearMealTypeListStateAction = createAction(
  `[${actionCategory}] Clear State`
);
