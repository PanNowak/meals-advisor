import {Meal} from 'app/modules/meal/models';

export interface MealDetailsState {
  loadedMeal: Meal;
  currentMeal: Meal;
}

const nullMeal: Meal = {
  id: null,
  name: '',
  mealTypes: [],
  ingredients: []
};

export const initialState: MealDetailsState = {
  loadedMeal: nullMeal,
  currentMeal: nullMeal
};
