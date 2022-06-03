import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {Product} from 'app/modules/product/models';
import {cancelProductDetailsAddAction, productDetailsAddRequest} from 'app/modules/product/store';

@Component({
  selector: 'app-product-add',
  templateUrl: './product-add.component.html',
})
export class ProductAddComponent {

  constructor(private store: Store<AppState>) { }

  saveProduct(product: Product): void {
    this.store.dispatch(productDetailsAddRequest({productToAdd: product}));
  }

  closeDialog(): void {
    this.store.dispatch(cancelProductDetailsAddAction());
  }
}
