import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSelectModule} from '@angular/material/select';
import {MatTableModule} from '@angular/material/table';
import {MatTooltipModule} from '@angular/material/tooltip';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {
  IngredientAddDialogComponent,
  IngredientTableComponent,
  MealAddComponent,
  MealDetailsComponent,
  MealEditComponent,
  MealListComponent
} from 'app/modules/meal/components';
import {mealEffects} from 'app/modules/meal/effects';
import {mealStateFeatureKey, mealStateReducers} from 'app/modules/meal/store';

@NgModule({
  declarations: [
    MealListComponent,
    MealAddComponent,
    MealEditComponent,
    MealDetailsComponent,
    IngredientTableComponent,
    IngredientAddDialogComponent
  ],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatTableModule,
    MatButtonModule,
    MatTooltipModule,
    MatPaginatorModule,
    MatSelectModule,
    MatDialogModule,
    FormsModule,
    ReactiveFormsModule,
    StoreModule.forFeature(mealStateFeatureKey, mealStateReducers),
    EffectsModule.forFeature(mealEffects)
  ]
})
export class MealModule { }
