import {createReducer, on} from '@ngrx/store';
import {Ingredient, Meal, MealType} from 'app/modules/meal/models';
import {
  addIngredientAction,
  clearMealDetailsStateAction,
  removeIngredientAction,
  setChosenMealTypesAction,
  setMealDetailsAction,
  setMealNameAction
} from 'app/modules/meal/store/meal-details/meal-details.actions';
import {initialState, MealDetailsState} from 'app/modules/meal/store/meal-details/meal-details.state';

export const mealDetailsStateReducer = createReducer(initialState,
  on(clearMealDetailsStateAction, () => initialState),
  on(setMealDetailsAction, setLoadedMeal),
  on(setMealNameAction, setMealName),
  on(setChosenMealTypesAction, setChosenMealTypes),
  on(addIngredientAction, addIngredient),
  on(removeIngredientAction, removeIngredient)
);

function setLoadedMeal(state: MealDetailsState,
                       action: { meal: Meal; }): MealDetailsState {
  return {
    ...state,
    loadedMeal: action.meal,
    currentMeal: action.meal
  };
}

function setMealName(state: MealDetailsState,
                     action: { newName: string; }): MealDetailsState {
  return {
    ...state,
    currentMeal: {
      ...state.currentMeal,
      name: action.newName
    }
  };
}

function setChosenMealTypes(state: MealDetailsState,
                            action: { chosenMealTypes: MealType[] }): MealDetailsState {
  return {
    ...state,
    currentMeal: {
      ...state.currentMeal,
      mealTypes: action.chosenMealTypes
    }
  };
}

function addIngredient(state: MealDetailsState,
                       action: { ingredientToAdd: Ingredient; }): MealDetailsState {
  const currentMeal = state.currentMeal;
  const previousIngredients = currentMeal.ingredients;
  const updatedIngredients = previousIngredients.concat(action.ingredientToAdd);

  return {
    ...state,
    currentMeal: {
      ...currentMeal,
      ingredients: updatedIngredients
    }
  };
}

function removeIngredient(state: MealDetailsState,
                          action: { ingredientToRemove: Ingredient; }): MealDetailsState {
  const currentMeal = state.currentMeal;
  const previousIngredients = currentMeal.ingredients;
  const updatedIngredients = previousIngredients
    .filter(ingredient => action.ingredientToRemove !== ingredient);

  return {
    ...state,
    currentMeal: {
      ...currentMeal,
      ingredients: updatedIngredients
    }
  };
}
