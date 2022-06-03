import {ProductSummary} from 'app/modules/product/models';
import {Unit} from 'app/modules/unit/models';

export interface GroceryItem {
  productSummary: ProductSummary;
  numberOfUnits: number;
  unit: Unit;
}
