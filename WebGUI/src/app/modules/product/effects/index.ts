import {ProductCacheEffects} from 'app/modules/product/effects/product-cache.effects';
import {ProductCrudEffects} from 'app/modules/product/effects/product-crud.effects';
import {ProductDialogEffects} from 'app/modules/product/effects/product-dialog.effects';
import {ProductResultEffects} from 'app/modules/product/effects/product-result.effects';

export const productEffects = [
  ProductCacheEffects,
  ProductCrudEffects,
  ProductDialogEffects,
  ProductResultEffects
];
