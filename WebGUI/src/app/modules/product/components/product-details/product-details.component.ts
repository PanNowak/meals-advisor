import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {Product} from 'app/modules/product/models';
import {
  getAvailablePrimaryUnits,
  getCurrentProduct,
  getLoadedProduct,
  isCurrentProductIdenticalToLoaded,
  setPrimaryUnitAction,
  setProductNameAction
} from 'app/modules/product/store';
import {ProductValidatorsFactory} from 'app/modules/product/validators/product-validators.factory';
import {errorObservable, selectedValueObservable, textInputObservable} from 'app/modules/shared/control.observables';
import {areUnitsEqual, Unit} from 'app/modules/unit/models';
import {isNotNullOrUndefined} from 'codelyzer/util/isNotNullOrUndefined';
import {combineLatest, identity, Observable, Subscription} from 'rxjs';
import {first, map} from 'rxjs/operators';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit, OnDestroy {

  @Output()
  save = new EventEmitter<Product>();

  @Output()
  cancel = new EventEmitter<void>();

  nameControl = new FormControl('', Validators.required, this.validatorsFactory.uniqueName());

  availablePrimaryUnits$: Observable<Unit[]>;
  primaryUnitControl = new FormControl('', Validators.required);

  validationMessage$: Observable<string>;
  isValidationFailed$: Observable<boolean>;

  private loadedProductSubscription: Subscription;
  private controlsChangesSubscription = new Subscription();

  constructor(private store: Store<AppState>,
              private validatorsFactory: ProductValidatorsFactory) {}

  ngOnInit(): void {
    this.setUpDataLoadLogic();
    this.setUpControlsChangesObservers();
    this.setUpValidation();
  }

  ngOnDestroy(): void {
    this.loadedProductSubscription.unsubscribe();
    this.controlsChangesSubscription.unsubscribe();
  }

  saveProduct(): void {
    this.store.select(getCurrentProduct)
      .pipe(first())
      .subscribe(product => this.save.emit(product));
  }

  closeView(): void {
    this.cancel.emit();
  }

  areUnitsEqual(unit1: Unit, unit2: Unit): boolean {
    return areUnitsEqual(unit1, unit2);
  }

  private setUpDataLoadLogic(): void {
    this.loadedProductSubscription = this.store.select(getLoadedProduct)
      .subscribe(product => this.assignProductData(product));

    this.availablePrimaryUnits$ = this.store.select(getAvailablePrimaryUnits);
  }

  private assignProductData(product: Product): void {
    this.nameControl.setValue(product.name);
    this.primaryUnitControl.setValue(product.primaryUnit);
  }

  private setUpControlsChangesObservers(): void {
    this.controlsChangesSubscription.add(textInputObservable(this.nameControl)
      .subscribe(productName => this.store.dispatch(setProductNameAction({newName: productName}))));

    this.controlsChangesSubscription.add(selectedValueObservable(this.primaryUnitControl)
      .subscribe(primaryUnit => this.store.dispatch(setPrimaryUnitAction({newPrimaryUnit: primaryUnit}))));
  }

  private setUpValidation(): void {
    this.validationMessage$ = this.observeValidationMessages();
    this.isValidationFailed$ = this.validationMessage$.pipe(map(isNotNullOrUndefined));
  }

  private observeValidationMessages(): Observable<string> {
    return combineLatest([
      this.wrongInputErrorMessage(),
      this.noChangesErrorMessage()
    ]).pipe(
      map(errorMessages => errorMessages.find(isNotNullOrUndefined))
    );
  }

  private wrongInputErrorMessage(): Observable<string> {
    return combineLatest([
      errorObservable(this.nameControl, 'required'),
      errorObservable(this.nameControl, 'duplicate'),
      errorObservable(this.primaryUnitControl, 'required')
    ]).pipe(
      map(isErrorArray =>
        isErrorArray.some(identity) ? 'Wypełnij prawidłowo wszystkie obowiązkowe pola' : null)
    );
  }

  private noChangesErrorMessage(): Observable<string> {
    return this.store.select(isCurrentProductIdenticalToLoaded).pipe(
      map(isIdentical => isIdentical ? 'Nie wprowadzono żadnych zmian' : null)
    );
  }
}
