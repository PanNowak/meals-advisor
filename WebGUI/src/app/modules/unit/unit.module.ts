import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {MatTableModule} from '@angular/material/table';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {UnitComponent} from 'app/modules/unit/components';
import {unitEffects} from 'app/modules/unit/effects';
import {unitStateFeatureKey, unitStateReducers} from 'app/modules/unit/store';

@NgModule({
  declarations: [
    UnitComponent
  ],
  imports: [
    MatTableModule,
    CommonModule,
    StoreModule.forFeature(unitStateFeatureKey, unitStateReducers),
    EffectsModule.forFeature(unitEffects)
  ]
})
export class UnitModule {}
