import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {areMealTypesEqual, Meal, MealType} from 'app/modules/meal/models';
import {
  getChosenIngredients,
  getCurrentMeal,
  getLoadedMeal,
  getMealTypeList,
  setChosenMealTypesAction,
  setMealNameAction
} from 'app/modules/meal/store';
import {selectedValueObservable, textInputObservable} from 'app/modules/shared/control.observables';
import {Observable, Subscription} from 'rxjs';
import {first, map} from 'rxjs/operators';

@Component({
  selector: 'app-meal-details',
  templateUrl: './meal-details.component.html',
  styleUrls: ['./meal-details.component.css']
})
export class MealDetailsComponent implements OnInit, OnDestroy {

  @Output()
  save = new EventEmitter<Meal>();

  @Output()
  cancel = new EventEmitter<void>();

  nameControl = new FormControl('', [Validators.required]);

  allMealTypes$: Observable<MealType[]>;
  mealTypesControl = new FormControl('', [Validators.required]);

  noIngredientsAdded$: Observable<boolean>;

  private loadedMealSubscription: Subscription;
  private controlsChangesSubscription = new Subscription();

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.setUpDataLoadLogic();
    this.setUpControlsChangesObservers();

    this.noIngredientsAdded$ = this.store.select(getChosenIngredients)
      .pipe(map(ingredients => ingredients.length === 0));
  }

  ngOnDestroy(): void {
    this.loadedMealSubscription.unsubscribe();
    this.controlsChangesSubscription.unsubscribe();
  }

  saveMeal(): void {
    this.store.select(getCurrentMeal)
      .pipe(first())
      .subscribe(meal => this.save.emit(meal));
  }

  closeView(): void {
    this.cancel.emit();
  }

  isAnyInputMissing(): boolean {
    return this.nameControl.hasError('required') ||
      this.mealTypesControl.hasError('required');
  }

  areMealTypesEqual(mealType1: MealType, mealType2: MealType): boolean {
    return areMealTypesEqual(mealType1, mealType2);
  }

  private setUpDataLoadLogic(): void {
    this.loadedMealSubscription = this.store.select(getLoadedMeal)
      .subscribe(meal => this.assignMealData(meal));

    this.allMealTypes$ = this.store.select(getMealTypeList);
  }

  private assignMealData(meal: Meal): void {
    this.nameControl.setValue(meal.name);
    this.mealTypesControl.setValue(meal.mealTypes);
  }

  private setUpControlsChangesObservers(): void {
    this.controlsChangesSubscription.add(textInputObservable(this.nameControl)
      .subscribe(productName => this.store.dispatch(setMealNameAction({newName: productName}))));

    this.controlsChangesSubscription.add(selectedValueObservable(this.mealTypesControl)
      .subscribe(mealTypes => this.store.dispatch(setChosenMealTypesAction({chosenMealTypes: mealTypes}))));
  }
}
