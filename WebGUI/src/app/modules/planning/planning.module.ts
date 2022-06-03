import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {MatTableModule} from '@angular/material/table';
import {MatTooltipModule} from '@angular/material/tooltip';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {MealPlanComponent, ShoppingListComponent} from 'app/modules/planning/components';
import {planningEffects} from 'app/modules/planning/effects';
import {planningStateFeatureKey, planningStateReducers} from 'app/modules/planning/store';

@NgModule({
  declarations: [
    MealPlanComponent,
    ShoppingListComponent
  ],
  imports: [
    CommonModule,
    MatDialogModule,
    MatTableModule,
    MatButtonModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatTooltipModule,
    StoreModule.forFeature(planningStateFeatureKey, planningStateReducers),
    EffectsModule.forFeature(planningEffects)
  ]
})
export class PlanningModule { }
