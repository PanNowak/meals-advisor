import {createReducer, on} from '@ngrx/store';
import {Product, SecondaryUnitInfo} from 'app/modules/product/models';
import {
  addSecondaryUnitAction,
  clearProductDetailsStateAction,
  removeSecondaryUnitAction,
  setPrimaryUnitAction,
  setProductDetailsAction,
  setProductNameAction
} from 'app/modules/product/store/product-details/product-details.actions';
import {initialState, ProductDetailsState} from 'app/modules/product/store/product-details/product-details.state';
import {Unit} from 'app/modules/unit/models';

export const productDetailsStateReducer = createReducer(initialState,
  on(clearProductDetailsStateAction, () => initialState),
  on(setProductDetailsAction, setLoadedProduct),
  on(setProductNameAction, setProductName),
  on(setPrimaryUnitAction, setPrimaryUnit),
  on(addSecondaryUnitAction, addSecondaryUnit),
  on(removeSecondaryUnitAction, removeSecondaryUnit)
);

function setLoadedProduct(state: ProductDetailsState,
                          action: { product: Product; }): ProductDetailsState {
  return {
    ...state,
    loadedProduct: action.product,
    currentProduct: action.product
  };
}

function setProductName(state: ProductDetailsState,
                        action: { newName: string; }): ProductDetailsState {
  return {
    ...state,
    currentProduct: {
      ...state.currentProduct,
      name: action.newName
    }
  };
}

function setPrimaryUnit(state: ProductDetailsState,
                        action: { newPrimaryUnit: Unit; }): ProductDetailsState {
  return {
    ...state,
    currentProduct: {
      ...state.currentProduct,
      primaryUnit: action.newPrimaryUnit
    }
  };
}

function addSecondaryUnit(state: ProductDetailsState,
                          action: { secondaryUnitToAdd: SecondaryUnitInfo; }): ProductDetailsState {
  const currentProduct = state.currentProduct;
  const previousSecondaryUnits = currentProduct.secondaryUnits;
  const updatedSecondaryUnits = previousSecondaryUnits.concat(action.secondaryUnitToAdd);

  return {
    ...state,
    currentProduct: {
      ...currentProduct,
      secondaryUnits: updatedSecondaryUnits
    }
  };
}

function removeSecondaryUnit(state: ProductDetailsState,
                             action: { secondaryUnitToRemove: SecondaryUnitInfo; }): ProductDetailsState {
  const currentProduct = state.currentProduct;
  const previousSecondaryUnits = currentProduct.secondaryUnits;
  const updatedSecondaryUnits = previousSecondaryUnits
    .filter(unit => action.secondaryUnitToRemove !== unit);

  return {
    ...state,
    currentProduct: {
      ...currentProduct,
      secondaryUnits: updatedSecondaryUnits
    }
  };
}
