import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {Meal} from 'app/modules/meal/models';
import {cancelMealDetailsAddAction, mealDetailsAddRequest} from 'app/modules/meal/store';

@Component({
  selector: 'app-meal-add',
  templateUrl: './meal-add.component.html'
})
export class MealAddComponent {

  constructor(private store: Store<AppState>) { }

  saveMeal(meal: Meal): void {
    this.store.dispatch(mealDetailsAddRequest({mealToAdd: meal}));
  }

  closeDialog(): void {
    this.store.dispatch(cancelMealDetailsAddAction());
  }
}
