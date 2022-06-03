import {createAction, props} from '@ngrx/store';
import {Product, SecondaryUnitInfo} from 'app/modules/product/models';
import {Unit} from 'app/modules/unit/models';

const actionCategory = 'Product Details';

export const clearProductDetailsStateAction = createAction(
  `[${actionCategory}] Clear State`
);

export const productDetailsLoadRequest = createAction(
  `[${actionCategory}] Load Request`, props<{ productId: number; }>()
);

export const productDetailsFetchRequest = createAction(
  `[${actionCategory}] Fetch Request`, props<{ productId: number; }>()
);

export const productDetailsFetchSuccess = createAction(
  `[${actionCategory}] Fetch Success`, props<{ loadedProduct: Product; }>()
);

export const productDetailsFetchFailure = createAction(
  `[${actionCategory}] Fetch Failure`, props<{ error: Error; }>()
);

export const setProductDetailsAction = createAction(
  `[${actionCategory}] Set Product`, props<{ product: Product; }>()
);

export const setProductNameAction = createAction(
  `[${actionCategory}] Set Name`, props<{ newName: string; }>()
);

export const setPrimaryUnitAction = createAction(
  `[${actionCategory}] Set Primary Unit`, props<{ newPrimaryUnit: Unit; }>()
);

export const openAddSecondaryUnitDialogAction = createAction(
  `[${actionCategory}] Open Add Secondary Unit Dialog`
);

export const addSecondaryUnitAction = createAction(
  `[${actionCategory}] Add Secondary Unit`,
  props<{ secondaryUnitToAdd: SecondaryUnitInfo; }>()
);

export const removeSecondaryUnitAction = createAction(
  `[${actionCategory}] Remove Secondary Unit`,
  props<{ secondaryUnitToRemove: SecondaryUnitInfo; }>()
);

export const productDetailsUpdateRequest = createAction(
  `[${actionCategory}] Update Request`, props<{ productToUpdate: Product; }>()
);

export const productDetailsUpdateSuccess = createAction(
  `[${actionCategory}] Update Success`, props<{ updatedProduct: Product; }>()
);

export const productDetailsUpdateFailure = createAction(
  `[${actionCategory}] Update Failure`, props<{ error: Error; }>()
);

export const cancelProductDetailsUpdateAction = createAction(
  `[${actionCategory}] Cancel Update`
);

export const navigateToProductListViewAction = createAction(
  `[${actionCategory}] Navigate to List View`, props<{ isStateSaved?: boolean; }>()
);

export const productDetailsAddRequest = createAction(
  `[${actionCategory}] Add Request`, props<{ productToAdd: Product; }>()
);

export const productDetailsAddSuccess = createAction(
  `[${actionCategory}] Add Success`, props<{ addedProduct: Product; }>()
);

export const productDetailsAddFailure = createAction(
  `[${actionCategory}] Add Failure`, props<{ error: Error; }>()
);

export const cancelProductDetailsAddAction = createAction(
  `[${actionCategory}] Cancel Add`
);
