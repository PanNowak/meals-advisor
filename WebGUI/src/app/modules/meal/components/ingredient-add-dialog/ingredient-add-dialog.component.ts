import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {Ingredient} from 'app/modules/meal/models';
import {getAvailableProducts} from 'app/modules/meal/store';
import {areProductSummariesEqual, ProductSummary} from 'app/modules/product/models';
import {getAllUnitsOfLoadedProduct, productDetailsLoadRequest} from 'app/modules/product/store';
import {selectedValueObservable} from 'app/modules/shared/control.observables';
import {areUnitsEqual, Unit} from 'app/modules/unit/models';
import {Observable, Subscription} from 'rxjs';
import {filter} from 'rxjs/operators';

@Component({
  selector: 'app-ingredient-add-dialog',
  templateUrl: './ingredient-add-dialog.component.html'
})
export class IngredientAddDialogComponent implements OnInit, OnDestroy {

  availableProducts$: Observable<ProductSummary[]>;
  selectedProductControl = new FormControl('', [
    Validators.required,
  ]);

  amountControl = new FormControl('', [
    Validators.required,
    Validators.pattern('^([0-9]?|([1-9][0-9]*))(\\.[0-9]+)?$'),
  ]);

  availableUnits: Unit[];
  selectedUnitControl = new FormControl({value: '', disabled: true}, [
    Validators.required,
  ]);

  private availableUnitsSubscription: Subscription;
  private controlsChangesSubscription: Subscription;

  constructor(public dialogRef: MatDialogRef<IngredientAddDialogComponent>,
              private store: Store<AppState>) {}

  ngOnInit(): void {
    this.availableProducts$ = this.store.select(getAvailableProducts);

    this.availableUnitsSubscription = this.store.select(getAllUnitsOfLoadedProduct)
      .pipe(filter(units => units.length > 0))
      .subscribe(units => {
        this.availableUnits = units;
        this.selectedUnitControl.setValue(units[0]);
        this.selectedUnitControl.enable();
      });
    this.controlsChangesSubscription = selectedValueObservable(this.selectedProductControl)
      .subscribe(product => this.store.dispatch(productDetailsLoadRequest({productId: product.id})));
  }

  ngOnDestroy(): void {
    this.availableUnitsSubscription.unsubscribe();
    this.controlsChangesSubscription.unsubscribe();
  }

  addIngredient(): void {
    const ingredient = this.createIngredient();
    this.dialogRef.close(ingredient);
  }

  isAnyInputIncorrect(): boolean {
    return this.selectedProductControl.hasError('required') ||
      this.amountControl.hasError('required') ||
      this.amountControl.hasError('pattern') ||
      this.selectedUnitControl.hasError('required');
  }

  areProductsEqual(product1: ProductSummary, product2: ProductSummary): boolean {
    return areProductSummariesEqual(product1, product2);
  }

  areUnitsEqual(unit1: Unit, unit2: Unit): boolean {
    return areUnitsEqual(unit1, unit2);
  }

  private createIngredient(): Ingredient {
    return {
      id: null,
      numberOfUnits: parseFloat(this.amountControl.value),
      product: this.selectedProductControl.value,
      unit: this.selectedUnitControl.value
    };
  }
}
