import {createEntityAdapter, EntityState} from '@ngrx/entity';
import {Product} from 'app/modules/product/models';
import {CacheRecord} from 'app/modules/shared/cache.record';
import {comparingDates} from 'app/modules/shared/comparers';

export const productDetailsCacheAdapter = createEntityAdapter<CacheRecord<Product>>(
  {selectId: record => record.data.id,
    sortComparer: comparingDates(record => record.createdOn)});

export interface ProductDetailsCacheState extends EntityState<CacheRecord<Product>> {}

export const initialState: ProductDetailsCacheState = productDetailsCacheAdapter.getInitialState();
