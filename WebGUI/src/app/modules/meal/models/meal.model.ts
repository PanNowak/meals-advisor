import {areIngredientsEqual, Ingredient} from 'app/modules/meal/models/ingredient.model';
import {areMealTypesEqual, MealType} from 'app/modules/meal/models/meal-type.model';

export interface Meal {
  id: number;
  name: string;
  ingredients: Ingredient[];
  mealTypes: MealType[];
}

export function areMealsEqual(meal1: Meal, meal2: Meal): boolean {
  if (meal1 === meal2) {
    return true;
  }

  return meal1 && meal2 &&
    meal1.name === meal2.name &&
    areMealTypeCollectionsEqual(meal1.mealTypes, meal2.mealTypes) &&
    areIngredientCollectionsEqual(meal1.ingredients, meal2.ingredients);
}

function areMealTypeCollectionsEqual(mealTypes1: MealType[],
                                     mealTypes2: MealType[]): boolean {
  if (mealTypes1.length !== mealTypes2.length) {
    return false;
  }

  return mealTypes1.every(mealType1 =>
    mealTypes2.some(mealType2 =>
      areMealTypesEqual(mealType1, mealType2)));
}

function areIngredientCollectionsEqual(ingredients1: Ingredient[],
                                       ingredients2: Ingredient[]): boolean {
  if (ingredients1.length !== ingredients2.length) {
    return false;
  }

  return ingredients1.every(ingredient1 =>
    ingredients2.some(ingredient2 =>
      areIngredientsEqual(ingredient1, ingredient2)));
}
