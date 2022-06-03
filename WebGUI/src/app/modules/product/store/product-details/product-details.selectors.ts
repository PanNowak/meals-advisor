import {createSelector} from '@ngrx/store';
import {areProductsEqual, Product, SecondaryUnitInfo} from 'app/modules/product/models';
import {getProductState} from 'app/modules/product/store/product.selectors';
import {comparingStrings} from 'app/modules/shared/comparers';
import {Unit} from 'app/modules/unit/models';
import {getUnitList} from 'app/modules/unit/store';

export const getProductDetailsState = createSelector(getProductState,
    productState => productState.productDetailsState);

export const getLoadedProduct = createSelector(getProductDetailsState,
  productDetailsState => productDetailsState.loadedProduct);

export const getLoadedProductName = createSelector(getLoadedProduct,
  loadedProduct => loadedProduct.name);

export const getAllUnitsOfLoadedProduct = createSelector(
  getLoadedProduct, extractAllUnitsFromProduct);

export const getCurrentProduct = createSelector(getProductDetailsState,
  productDetailsState => productDetailsState.currentProduct);

export const getChosenPrimaryUnit = createSelector(getCurrentProduct,
  currentProduct => currentProduct.primaryUnit);

export const getChosenSecondaryUnits = createSelector(
  getCurrentProduct, extractSortedSecondaryUnits);

export const getAvailablePrimaryUnits = createSelector(
  getChosenSecondaryUnits, getUnitList, extractAvailablePrimaryUnits);

export const getAvailableSecondaryUnits = createSelector(
  getChosenPrimaryUnit, getAvailablePrimaryUnits, extractAvailableSecondaryUnits);

export const isCurrentProductIdenticalToLoaded = createSelector(
  getCurrentProduct, getLoadedProduct, areProductsEqual);


function extractAllUnitsFromProduct(product: Product): Unit[] {
  const primaryUnit = product.primaryUnit;
  const secondaryUnits = product.secondaryUnits.map(s => s.unit);

  const allUnits = primaryUnit ? secondaryUnits.concat(primaryUnit) : secondaryUnits;
  return allUnits.sort(comparingStrings(u => u.name));
}

function extractSortedSecondaryUnits(product: Product): SecondaryUnitInfo[] {
  const secondaryUnits = [...product.secondaryUnits];
  return secondaryUnits.sort(comparingStrings(s => s.unit.name));
}

function extractAvailablePrimaryUnits(chosenSecondaryUnits: SecondaryUnitInfo[],
                                      allUnits: Unit[]): Unit[] {
  if (chosenSecondaryUnits.length === 0) {
    return allUnits;
  }

  const secondaryUnitsIds = new Set(chosenSecondaryUnits.map(secondaryUnit => secondaryUnit.unit.id));
  return allUnits.filter(unit => !secondaryUnitsIds.has(unit.id));
}

function extractAvailableSecondaryUnits(chosenPrimaryUnit: Unit,
                                        availablePrimaryUnits: Unit[]): Unit[] {
  const currentPrimaryUnitId: number = chosenPrimaryUnit?.id;
  return currentPrimaryUnitId ?
    availablePrimaryUnits.filter(unit => currentPrimaryUnitId !== unit.id) :
    availablePrimaryUnits;
}
