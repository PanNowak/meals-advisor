import {Routes} from '@angular/router';
import {HomeComponent} from 'app/modules/home/home.component';
import {MealEditComponent, MealListComponent} from 'app/modules/meal/components';
import {MealEditLeaveConfirmationGuard} from 'app/modules/meal/guards';
import {MealEditResolver, MealListResolver} from 'app/modules/meal/resolvers';
import {MealPlanComponent} from 'app/modules/planning/components';
import {MealPlanResolver} from 'app/modules/planning/resolvers';
import {ProductEditComponent, ProductListComponent} from 'app/modules/product/components';
import {ProductEditLeaveConfirmationGuard} from 'app/modules/product/guards';
import {ProductEditResolver, ProductListResolver} from 'app/modules/product/resolvers';

export const homePath = 'home';
export const planningPath = 'planning';
export const mealPath = 'meals';
export const productPath = 'products';

export const routes: Routes = [
  {
    path: homePath,
    component: HomeComponent
  },
  {
    path: planningPath,
    component: MealPlanComponent,
    resolve: {mealPlan: MealPlanResolver}
  },
  {
    path: mealPath,
    component: MealListComponent,
    resolve: {mealList: MealListResolver}
  },
  {
    path: `${mealPath}/:id`,
    component: MealEditComponent,
    resolve: {mealEdit: MealEditResolver},
    canDeactivate: [MealEditLeaveConfirmationGuard]
  },
  {
    path: productPath,
    component: ProductListComponent,
    resolve: {productList: ProductListResolver}
  },
  {
    path: `${productPath}/:id`,
    component: ProductEditComponent,
    resolve: {productEdit: ProductEditResolver},
    canDeactivate: [ProductEditLeaveConfirmationGuard]
  },
  {
    path: '**',
    redirectTo: 'home',
    pathMatch: 'full'
  }

  // {path: 'units', component: UnitComponent},
  // {path: 'products/:id', canActivate: [ProductDetailGuard], component: ProductDetailComponent}
];
