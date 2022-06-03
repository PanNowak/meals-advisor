import {createAction, props} from '@ngrx/store';
import {ProductSummary} from 'app/modules/product/models';

const actionCategory = 'Product List';

export const productListLoadRequest = createAction(
  `[${actionCategory}] Load Request`
);

export const productListFetchRequest = createAction(
  `[${actionCategory}] Fetch Request`
);

export const productListFetchSuccess = createAction(
  `[${actionCategory}] Fetch Success`, props<{ products: ProductSummary[]; }>()
);

export const productListFetchFailure = createAction(
  `[${actionCategory}] Fetch Failure`, props<{ error: Error; }>()
);

export const setProductListAction = createAction(
  `[${actionCategory}] Set List`, props<{ products: ProductSummary[]; }>()
);

export const clearProductListStateAction = createAction(
  `[${actionCategory}] Clear State`
);

export const navigateToProductDetailsViewAction = createAction(
  `[${actionCategory}] Navigate to Details View`, props<{ requestedProductId: number; }>()
);

export const openAddProductDialogAction = createAction(
  `[${actionCategory}] Open Add Product Dialog`
);

export const closeAddProductDialogAction = createAction(
  `[${actionCategory}] Close Add Product Dialog`
);

export const openDeleteProductDialogAction = createAction(
  `[${actionCategory}] Open Delete Product Dialog`,
  props<{ productToDelete: ProductSummary; }>()
);

export const productDeleteRequest = createAction(
  `[${actionCategory}] Delete Request`, props<{ productToDelete: ProductSummary; }>()
);

export const productDeleteSuccess = createAction(
  `[${actionCategory}] Delete Success`, props<{ deletedProduct: ProductSummary; }>()
);

export const productDeleteFailure = createAction(
  `[${actionCategory}] Delete Failure`, props<{ error: Error; }>()
);
