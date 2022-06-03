import {createSelector} from '@ngrx/store';
import {areMealsEqual, Ingredient, Meal} from 'app/modules/meal/models';
import {getMealState} from 'app/modules/meal/store/meal.selectors';
import {ProductSummary} from 'app/modules/product/models';
import {getProductList} from 'app/modules/product/store';
import {comparingStrings} from 'app/modules/shared/comparers';

export const getMealDetailsState = createSelector(getMealState,
  mealState => mealState.mealDetailsState);

export const getLoadedMeal = createSelector(getMealDetailsState,
  mealDetailsState => mealDetailsState.loadedMeal);

export const getCurrentMeal = createSelector(getMealDetailsState,
  mealDetailsState => mealDetailsState.currentMeal);

export const getChosenIngredients = createSelector(
  getCurrentMeal, extractSortedIngredients);

export const getAvailableProducts = createSelector(
  getChosenIngredients, getProductList, extractAvailableProducts);

export const isCurrentMealIdenticalToLoaded = createSelector(
  getCurrentMeal, getLoadedMeal, areMealsEqual);


function extractSortedIngredients(meal: Meal): Ingredient[] {
  const ingredients = [...meal.ingredients];
  return ingredients.sort(comparingStrings(i => i.product.name));
}

function extractAvailableProducts(chosenIngredients: Ingredient[],
                                  allProducts: ProductSummary[]): ProductSummary[] {
  if (chosenIngredients.length === 0) {
    return allProducts;
  }

  const chosenProductsIds = new Set(chosenIngredients.map(ingredient => ingredient.product.id));
  return allProducts.filter(product => !chosenProductsIds.has(product.id));
}
