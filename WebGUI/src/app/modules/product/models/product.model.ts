import {areSecondaryUnitsEqual, SecondaryUnitInfo} from 'app/modules/product/models/secondary-unit-info.model';
import {areUnitsEqual, Unit} from 'app/modules/unit/models';

export interface Product {
  id: number;
  name: string;
  primaryUnit: Unit;
  secondaryUnits: SecondaryUnitInfo[];
}

export function areProductsEqual(product1: Product, product2: Product): boolean {
  if (product1 === product2) {
    return true;
  }

  return product1 && product2 &&
    product1.name === product2.name &&
    areUnitsEqual(product1.primaryUnit, product2.primaryUnit) &&
    areSecondaryUnitCollectionsEqual(product1.secondaryUnits, product2.secondaryUnits);
}

function areSecondaryUnitCollectionsEqual(secondaryUnits1: SecondaryUnitInfo[],
                                          secondaryUnits2: SecondaryUnitInfo[]): boolean {
  if (secondaryUnits1.length !== secondaryUnits2.length) {
    return false;
  }

  return secondaryUnits1.every(unit1 =>
    secondaryUnits2.some(unit2 =>
      areSecondaryUnitsEqual(unit1, unit2)));
}
