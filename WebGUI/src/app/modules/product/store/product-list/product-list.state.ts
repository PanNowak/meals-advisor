import {createEntityAdapter, EntityState} from '@ngrx/entity';
import {ProductSummary} from 'app/modules/product/models';
import {comparingStrings} from 'app/modules/shared/comparers';

export const productListAdapter =
  createEntityAdapter<ProductSummary>({sortComparer: comparingStrings(product => product.name)});

export interface ProductListState extends EntityState<ProductSummary> {}

export const initialState: ProductListState = productListAdapter.getInitialState();
