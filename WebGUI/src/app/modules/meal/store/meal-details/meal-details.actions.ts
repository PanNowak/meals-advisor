import {createAction, props} from '@ngrx/store';
import {Ingredient, Meal, MealType} from 'app/modules/meal/models';

const actionCategory = 'Meal Details';

export const clearMealDetailsStateAction = createAction(
  `[${actionCategory}] Clear State`
);

export const mealDetailsLoadRequest = createAction(
  `[${actionCategory}] Load Request`, props<{ mealId: number; }>()
);

export const mealDetailsFetchSuccess = createAction(
  `[${actionCategory}] Fetch Success`, props<{ loadedMeal: Meal; }>()
);

export const mealDetailsFetchFailure = createAction(
  `[${actionCategory}] Fetch Failure`, props<{ error: Error; }>()
);

export const setMealDetailsAction = createAction(
  `[${actionCategory}] Set Meal`, props<{ meal: Meal; }>()
);

export const setMealNameAction = createAction(
  `[${actionCategory}] Set Name`, props<{ newName: string; }>()
);

export const setChosenMealTypesAction = createAction(
  `[${actionCategory}] Set Meal Types`, props<{ chosenMealTypes: MealType[]; }>()
);

export const openAddIngredientDialogAction = createAction(
  `[${actionCategory}] Open Add Ingredient Dialog`
);

export const addIngredientAction = createAction(
  `[${actionCategory}] Add Ingredient`,
  props<{ ingredientToAdd: Ingredient; }>()
);

export const removeIngredientAction = createAction(
  `[${actionCategory}] Remove Ingredient`,
  props<{ ingredientToRemove: Ingredient; }>()
);

export const mealDetailsUpdateRequest = createAction(
  `[${actionCategory}] Update Request`, props<{ mealToUpdate: Meal; }>()
);

export const mealDetailsUpdateSuccess = createAction(
  `[${actionCategory}] Update Success`, props<{ updatedMeal: Meal; }>()
);

export const mealDetailsUpdateFailure = createAction(
  `[${actionCategory}] Update Failure`, props<{ error: Error; }>()
);

export const cancelMealDetailsUpdateAction = createAction(
  `[${actionCategory}] Cancel Update`
);

export const navigateToMealListViewAction = createAction(
  `[${actionCategory}] Navigate to List View`, props<{ isStateSaved?: boolean; }>()
);

export const mealDetailsAddRequest = createAction(
  `[${actionCategory}] Add Request`, props<{ mealToAdd: Meal; }>()
);

export const mealDetailsAddSuccess = createAction(
  `[${actionCategory}] Add Success`, props<{ addedMeal: Meal; }>()
);

export const mealDetailsAddFailure = createAction(
  `[${actionCategory}] Add Failure`, props<{ error: Error; }>()
);

export const cancelMealDetailsAddAction = createAction(
  `[${actionCategory}] Cancel Add`
);
