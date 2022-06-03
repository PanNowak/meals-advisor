import {areProductsEqual, Product} from 'app/modules/product/models';
import {areUnitsEqual, Unit} from 'app/modules/unit/models';

export interface Ingredient {
  id: number;
  numberOfUnits: number;
  product: Product;
  unit: Unit;
}

export function areIngredientsEqual(ingredient1: Ingredient,
                                    ingredient2: Ingredient): boolean {
  if (ingredient1 === ingredient2) {
    return true;
  }

  return ingredient1 && ingredient2 &&
    ingredient1.numberOfUnits === ingredient2.numberOfUnits &&
    areUnitsEqual(ingredient1.unit, ingredient2.unit) &&
    areProductsEqual(ingredient1.product, ingredient2.product);
}
