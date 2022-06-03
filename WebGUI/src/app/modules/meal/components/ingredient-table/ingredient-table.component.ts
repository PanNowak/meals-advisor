import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {Ingredient} from 'app/modules/meal/models';
import {getAvailableProducts, getChosenIngredients, openAddIngredientDialogAction, removeIngredientAction} from 'app/modules/meal/store';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-ingredient-table',
  templateUrl: './ingredient-table.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class IngredientTableComponent implements OnInit {

  isNoProductAvailable$: Observable<boolean>;

  displayedColumns = ['product', 'amount', 'unit', 'deleteButton'];
  ingredients$: Observable<Ingredient[]>;

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.isNoProductAvailable$ = this.store.select(getAvailableProducts)
      .pipe(map(products => products.length === 0));

    this.ingredients$ = this.store.select(getChosenIngredients);
  }

  addIngredient(): void {
    this.store.dispatch(openAddIngredientDialogAction());
  }

  deleteIngredient(ingredient: Ingredient): void {
    this.store.dispatch(removeIngredientAction({ingredientToRemove: ingredient}));
  }
}
