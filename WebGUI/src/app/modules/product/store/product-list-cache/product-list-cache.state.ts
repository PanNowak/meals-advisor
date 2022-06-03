import {createEntityAdapter, EntityState} from '@ngrx/entity';
import {ProductSummary} from 'app/modules/product/models';
import {CacheState, initialCacheState} from 'app/modules/shared/cache.state';
import {comparingStrings} from 'app/modules/shared/comparers';

export const productListCacheAdapter =
  createEntityAdapter<ProductSummary>({sortComparer: comparingStrings(product => product.name)});

export interface ProductListCacheState extends EntityState<ProductSummary>, CacheState {}

export const initialState: ProductListCacheState = productListCacheAdapter.getInitialState(initialCacheState);
