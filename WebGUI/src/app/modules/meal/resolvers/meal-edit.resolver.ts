import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot} from '@angular/router';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {mealDetailsLoadRequest, mealTypeListLoadRequest, navigateToMealListViewAction} from 'app/modules/meal/store';
import {productListLoadRequest} from 'app/modules/product/store';
import {isValidId} from 'app/modules/shared/id-param.checker';
import {StateResolver} from 'app/modules/shared/state.resolver';

@Injectable({
  providedIn: 'root'
})
export class MealEditResolver implements StateResolver {

  constructor(private store: Store<AppState>) {}

  resolve(route: ActivatedRouteSnapshot): void {
    const requestedMealId = Number(route.paramMap.get('id'));
    if (isValidId(requestedMealId)) {
      this.store.dispatch(mealDetailsLoadRequest({mealId: requestedMealId}));
      this.store.dispatch(mealTypeListLoadRequest());
      this.store.dispatch(productListLoadRequest());
    } else {
      this.store.dispatch(navigateToMealListViewAction({isStateSaved: true}));
    }
  }
}
