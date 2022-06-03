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
  ProductAddComponent,
  ProductDetailsComponent,
  ProductEditComponent,
  ProductListComponent,
  SecondaryUnitAddDialogComponent,
  SecondaryUnitTableComponent
} from 'app/modules/product/components';
import {productEffects} from 'app/modules/product/effects';
import {productStateFeatureKey, productStateReducers} from 'app/modules/product/store';

@NgModule({
  declarations: [
    ProductListComponent,
    ProductAddComponent,
    ProductEditComponent,
    ProductDetailsComponent,
    SecondaryUnitTableComponent,
    SecondaryUnitAddDialogComponent
  ],
  imports: [
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatTableModule,
    ReactiveFormsModule,
    CommonModule,
    MatButtonModule,
    MatTooltipModule,
    MatIconModule,
    MatPaginatorModule,
    FormsModule,
    StoreModule.forFeature(productStateFeatureKey, productStateReducers),
    EffectsModule.forFeature(productEffects)
  ]
})
export class ProductModule {
}
