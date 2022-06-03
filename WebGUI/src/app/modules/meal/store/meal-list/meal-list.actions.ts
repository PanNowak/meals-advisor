import {createAction, props} from '@ngrx/store';
import {MealSummary} from 'app/modules/meal/models';

const actionCategory = 'Meal List';

export const mealListLoadRequest = createAction(
  `[${actionCategory}] Load Request`
);

export const mealListFetchRequest = createAction(
  `[${actionCategory}] Fetch Request`
);

export const mealListFetchSuccess = createAction(
  `[${actionCategory}] Fetch Success`, props<{ meals: MealSummary[]; }>()
);

export const mealListFetchFailure = createAction(
  `[${actionCategory}] Fetch Failure`, props<{ error: Error; }>()
);

export const setMealListAction = createAction(
  `[${actionCategory}] Set List`, props<{ meals: MealSummary[]; }>()
);

export const clearMealListStateAction = createAction(
  `[${actionCategory}] Clear State`
);

export const navigateToMealDetailsViewAction = createAction(
  `[${actionCategory}] Navigate to Details View`, props<{ requestedMealId: number; }>()
);

export const openAddMealDialogAction = createAction(
  `[${actionCategory}] Open Add Meal Dialog`
);

export const closeAddMealDialogAction = createAction(
  `[${actionCategory}] Close Add Meal Dialog`
);

export const openDeleteMealDialogAction = createAction(
  `[${actionCategory}] Open Delete Meal Dialog`,
  props<{ mealToDelete: MealSummary; }>()
);

export const mealDeleteRequest = createAction(
  `[${actionCategory}] Delete Request`, props<{ mealToDelete: MealSummary; }>()
);

export const mealDeleteSuccess = createAction(
  `[${actionCategory}] Delete Success`, props<{ deletedMeal: MealSummary; }>()
);

export const mealDeleteFailure = createAction(
  `[${actionCategory}] Delete Failure`, props<{ error: Error; }>()
);
