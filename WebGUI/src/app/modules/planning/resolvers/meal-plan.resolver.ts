import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot} from '@angular/router';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {mealTypeListLoadRequest} from 'app/modules/meal/store';
import {StateResolver} from 'app/modules/shared/state.resolver';

@Injectable({
  providedIn: 'root'
})
export class MealPlanResolver implements StateResolver {

  constructor(private store: Store<AppState>) {}

  resolve(route: ActivatedRouteSnapshot): void {
    this.store.dispatch(mealTypeListLoadRequest());
  }
}
