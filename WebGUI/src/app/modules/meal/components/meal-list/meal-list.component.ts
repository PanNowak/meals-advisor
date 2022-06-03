import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {MealSummary} from 'app/modules/meal/models';
import {
  getMealList,
  getMealListControlsState,
  navigateToMealDetailsViewAction,
  openAddMealDialogAction,
  openDeleteMealDialogAction,
  setMealPaginationInfoAction,
  setMealSearchValueAction
} from 'app/modules/meal/store';
import {MealListControlsState} from 'app/modules/meal/store/meal-list-controls/meal-list-controls.state';
import {ObservableBasedDataSource} from 'app/modules/shared/observable-based-data-source';
import {nameMatchesSearchValue} from 'app/modules/shared/search.predicate';
import {Subscription} from 'rxjs';
import {concatMapTo, filter, first, map} from 'rxjs/operators';

@Component({
  selector: 'app-meal-list',
  templateUrl: './meal-list.component.html'
})
export class MealListComponent implements OnInit, OnDestroy {

  readonly displayedColumns: string[] = ['mealName', 'deleteButton'];
  readonly meals = new ObservableBasedDataSource<MealSummary>();

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  private pageEventsSubscription = new Subscription();

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.setUpPaginator();
    this.setUpDataSourceLogic();
    this.loadPreviousControlsState();
  }

  ngOnDestroy(): void {
    this.pageEventsSubscription.unsubscribe();
  }

  showMealDetails(mealId: number): void {
    this.store.dispatch(navigateToMealDetailsViewAction({requestedMealId: mealId}));
  }

  addMeal(): void {
    this.store.dispatch(openAddMealDialogAction());
  }

  deleteMeal(meal: MealSummary): void {
    this.store.dispatch(openDeleteMealDialogAction({mealToDelete: meal}));
  }

  get searchValue(): string {
    return this.meals.filter;
  }

  set searchValue(value: string) {
    this.store.dispatch(setMealSearchValueAction({searchValue: value}));
    this.meals.filter = value;
  }

  private setUpPaginator(): void {
    const paginatorIntl = this.paginator._intl;
    paginatorIntl.itemsPerPageLabel = 'Posiłków na stronę';
    paginatorIntl.firstPageLabel = 'Pierwsza strona';
    paginatorIntl.previousPageLabel = 'Poprzednia strona';
    paginatorIntl.nextPageLabel = 'Następna strona';
    paginatorIntl.lastPageLabel = 'Ostatnia strona';
  }

  private setUpDataSourceLogic(): void {
    this.meals.observableData = this.store.select(getMealList);
    this.meals.filterPredicate = nameMatchesSearchValue;
    this.meals.paginator = this.paginator;
  }

  private loadPreviousControlsState(): void {
    this.store.select(getMealList).pipe(
      filter(meals => meals.length > 0),
      concatMapTo(this.store.select(getMealListControlsState)),
      first()
    ).subscribe(controlsState => {
      this.updateControls(controlsState);
      this.observePaginationEvents();
    });
  }

  private updateControls(controlsState: MealListControlsState): void {
    this.meals.filter = controlsState.searchValue;
    this.paginator.pageIndex = controlsState.pageIndex;
    this.paginator.pageSize = controlsState.pageSize;
    this.paginator.page.next({
      pageIndex: this.paginator.pageIndex,
      pageSize: this.paginator.pageSize,
      length: this.paginator.length
    });
  }

  private observePaginationEvents(): void {
    this.pageEventsSubscription = this.paginator.page.pipe(
      map(pageEvent => setMealPaginationInfoAction(
        {pageIndex: pageEvent.pageIndex, pageSize: pageEvent.pageSize}))
    ).subscribe(action => this.store.dispatch(action));
  }
}
