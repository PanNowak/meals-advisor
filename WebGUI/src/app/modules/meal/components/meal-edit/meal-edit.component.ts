import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {Meal} from 'app/modules/meal/models';
import {cancelMealDetailsUpdateAction, mealDetailsUpdateRequest} from 'app/modules/meal/store';

@Component({
  selector: 'app-meal-edit',
  templateUrl: './meal-edit.component.html'
})
export class MealEditComponent {

  constructor(private store: Store<AppState>) { }

  updateMeal(meal: Meal): void {
    this.store.dispatch(mealDetailsUpdateRequest({mealToUpdate: meal}));
  }

  switchToMealListView(): void {
    this.store.dispatch(cancelMealDetailsUpdateAction());
  }
}
