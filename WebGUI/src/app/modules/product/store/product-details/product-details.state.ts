import {Product} from 'app/modules/product/models';

export interface ProductDetailsState {
  loadedProduct: Product;
  currentProduct: Product;
}

const nullProduct: Product = {
  id: null,
  name: '',
  primaryUnit: null,
  secondaryUnits: []
};

export const initialState: ProductDetailsState = {
  loadedProduct: nullProduct,
  currentProduct: nullProduct
};
