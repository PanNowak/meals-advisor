import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot} from '@angular/router';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {navigateToProductListViewAction, productDetailsLoadRequest} from 'app/modules/product/store';
import {isValidId} from 'app/modules/shared/id-param.checker';
import {StateResolver} from 'app/modules/shared/state.resolver';
import {unitListLoadRequest} from 'app/modules/unit/store';

@Injectable({
  providedIn: 'root'
})
export class ProductEditResolver implements StateResolver {

  constructor(private store: Store<AppState>) {}

  resolve(route: ActivatedRouteSnapshot): void {
    const requestedProductId = Number(route.paramMap.get('id'));
    if (isValidId(requestedProductId)) {
      this.store.dispatch(productDetailsLoadRequest({productId: requestedProductId}));
      this.store.dispatch(unitListLoadRequest());
    } else {
      this.store.dispatch(navigateToProductListViewAction({isStateSaved: true}));
    }
  }
}
