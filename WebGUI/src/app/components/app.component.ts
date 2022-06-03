import {Component, OnInit} from '@angular/core';
import {Action, createAction, props, Store} from '@ngrx/store';
import {navigateToMealListViewAction} from 'app/modules/meal/store';
import {navigateToProductListViewAction} from 'app/modules/product/store';
import {isAnyTaskRunning} from 'app/modules/shared/store/background-task-count/background-task-count.selectors';
import {RoutingButtonInfo} from 'app/routing-button-info';
import {AppState} from 'app/store/app.state';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  title = 'Meals Advisor';
  imgLink: string;
  isLoading$: Observable<boolean>;

  routingButtonInfos = [
    new RoutingButtonInfo('Strona główna', 'pusheen.png', navigateToHomeViewAction({})),
    new RoutingButtonInfo('Planowanie', 'week-plan.png', navigateToPlanningViewAction({})),
    new RoutingButtonInfo('Posiłki', 'all-meals.png', navigateToMealListViewAction({})),
    new RoutingButtonInfo('Produkty', 'all-products.png', navigateToProductListViewAction({}))
  ];

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.isLoading$ = this.store.select(isAnyTaskRunning);
    this.imgLink = this.routingButtonInfos[0].imageLink;
  }

  navigate(navigationAction: Action): void {
    this.store.dispatch(navigationAction);
  }
}

export const navigateToHomeViewAction = createAction(
  `[Home Navigation] Navigate to Home View`, props<{ isStateSaved?: boolean; }>()
);

export const navigateToPlanningViewAction = createAction(
  `[Planning] Navigate to Planning View`, props<{ isStateSaved?: boolean; }>()
);
