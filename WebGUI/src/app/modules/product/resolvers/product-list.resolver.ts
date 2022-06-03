import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {productListLoadRequest} from 'app/modules/product/store';
import {StateResolver} from 'app/modules/shared/state.resolver';

@Injectable({
  providedIn: 'root'
})
export class ProductListResolver implements StateResolver {

  constructor(private store: Store<AppState>) {}

  resolve(): void {
    this.store.dispatch(productListLoadRequest());
  }
}
