import {createReducer, on} from '@ngrx/store';
import {DayPlan} from 'app/modules/planning/models';
import {setMealPlanAction} from 'app/modules/planning/store/meal-plan/meal-plan.actions';
import {initialState, mealPlanAdapter, MealPlanState} from 'app/modules/planning/store/meal-plan/meal-plan.state';

export const mealPlanStateReducer = createReducer(initialState,
  on(setMealPlanAction, setMealPlan)
);

function setMealPlan(state: MealPlanState, action: { dayPlans: DayPlan[]; }): MealPlanState {
  return mealPlanAdapter.setAll(action.dayPlans, state);
}
