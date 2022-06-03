import {createFeatureSelector} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {ProductState} from 'app/modules/product/store/product.state';

export const productStateFeatureKey = 'productState';

export const getProductState = createFeatureSelector<AppState, ProductState>(productStateFeatureKey);

