import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {EffectsModule} from '@ngrx/effects';
import {HomeComponent} from 'app/modules/home/home.component';
import {MealModule} from 'app/modules/meal/meal.module';
import {ProductModule} from 'app/modules/product/product.module';
import {RoutingEffects} from 'app/modules/routing/effects/routing.effects';
import {routes} from 'app/modules/routing/routings';
import {UnitModule} from 'app/modules/unit/unit.module';
import {PlanningModule} from 'app/modules/planning/planning.module';


@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    UnitModule,
    ProductModule,
    MealModule,
    PlanningModule,
    RouterModule.forRoot(routes, { initialNavigation: 'enabled', relativeLinkResolution: 'legacy' }),
    EffectsModule.forFeature([RoutingEffects])
  ],
  exports: [
    RouterModule
  ]
})
export class RoutingModule { }
