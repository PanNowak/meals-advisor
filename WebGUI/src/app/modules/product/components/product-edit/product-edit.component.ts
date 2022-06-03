import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {Product} from 'app/modules/product/models';
import {cancelProductDetailsUpdateAction, productDetailsUpdateRequest} from 'app/modules/product/store';

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html'
})
export class ProductEditComponent {

  constructor(private store: Store<AppState>) { }

  updateProduct(product: Product): void {
    this.store.dispatch(productDetailsUpdateRequest({productToUpdate: product}));
  }

  switchToProductListView(): void {
    this.store.dispatch(cancelProductDetailsUpdateAction());
  }
}
