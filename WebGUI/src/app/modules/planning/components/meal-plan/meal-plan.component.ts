import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {MealType} from 'app/modules/meal/models/meal-type.model';
import {getMealTypeList, getMealTypeNames} from 'app/modules/meal/store';
import {ShoppingListComponent} from 'app/modules/planning/components/shopping-list/shopping-list.component';
import {DayPlan, GroceryItem} from 'app/modules/planning/models';
import {getDayPlans, isMealPlanGenerated, mealPlanGenerationRequest} from 'app/modules/planning/store';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

class WeekDay {
  dayNumber: number;
  name: string;

  constructor(dayNumber: number, name: string) {
    this.dayNumber = dayNumber;
    this.name = name;
  }
}

@Component({
  selector: 'app-meal-plan-draw',
  templateUrl: './meal-plan.component.html',
  styleUrls: ['./meal-plan.component.css']
})
export class MealPlanComponent implements OnInit {

  weekDays: WeekDay[] = [
    new WeekDay(1, 'Poniedziałek'),
    new WeekDay(2, 'Wtorek'),
    new WeekDay(3, 'Środa'),
    new WeekDay(4, 'Czwartek'),
    new WeekDay(5, 'Piątek'),
    new WeekDay(6, 'Sobota'),
    new WeekDay(7, 'Niedziela')
  ];

  firstDayControl = new FormControl('', Validators.required);
  lastDayControl = new FormControl('', Validators.required);

  mealTypes$: Observable<MealType[]>;
  displayedColumns$: Observable<string[]>;
  dayPlans$: Observable<DayPlan[]>;
  isPlanAlreadyGenerated$: Observable<boolean>;

  shoppingList: GroceryItem[] = [];

  constructor(private dialog: MatDialog,
              private store: Store<AppState>) { }

  ngOnInit(): void {
    this.mealTypes$ = this.store.select(getMealTypeList);
    this.displayedColumns$ = this.store.select(getMealTypeNames)
      .pipe(map(mealTypeNames => ['dayOfWeek'].concat(mealTypeNames)));

    this.dayPlans$ = this.store.select(getDayPlans);
    this.isPlanAlreadyGenerated$ = this.store.select(isMealPlanGenerated);
  }

  getMealName(mealType: MealType, dayPlan: DayPlan): string {
    const mealTypeKey = `${mealType.id}. ${mealType.name}`;
    return dayPlan.mealsByType[mealTypeKey].name;
  }

  isAnySelectionMissing(): boolean {
    return this.firstDayControl.hasError('required') ||
      this.lastDayControl.hasError('required');
  }

  generatePlans(): void {
    this.shoppingList = [];
    this.store.dispatch(mealPlanGenerationRequest(
      {firstDay: this.firstDayControl.value, lastDay: this.lastDayControl.value}));
  }

  showShoppingList(): void {
    if (this.shoppingList.length === 0) {
      this.generateShoppingList();
    } else {
      this.showShoppingListDialog();
    }
  }

  private generateShoppingList(): void {
    // this.collectDrawnMealsIds()
    //   .pipe(
    //     mergeMap(ids => this.planningService.generateShoppingList(ids)),
    //     map(shoppingList => shoppingList
    //       .sort((item1, item2) => item1.productSummary.name.localeCompare(item2.productSummary.name, 'pl'))) // TODO
    //   )
    //   .subscribe(shoppingList => this.shoppingList = shoppingList,
    //     error => {
    //       this.snackBarFactory.showErrorSnackBar(error);
    //     },
    //     () => {
    //       this.showShoppingListDialog();
    //     });
  }

  private showShoppingListDialog(): void {
    this.dialog.open(ShoppingListComponent, {data: this.shoppingList, width: '100%'});
  }

  // private collectDrawnMealsIds(): Observable<number[]> {
  //   return from(this.dayPlans.data)
  //     .pipe(
  //       map(dayPlan => Object.values(dayPlan.mealsByType)),
  //       concatMap(meals => from(meals)),
  //       map(meal => meal.id),
  //       toArray()
  //     );
  // }
}
