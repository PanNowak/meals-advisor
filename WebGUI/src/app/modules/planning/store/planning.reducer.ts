import {ActionReducerMap} from '@ngrx/store';
import {mealPlanStateReducer} from 'app/modules/planning/store/meal-plan/meal-plan.reducer';
import {PlanningState} from 'app/modules/planning/store/planning.state';

export const planningStateReducers: ActionReducerMap<PlanningState> = {
  mealPlanState: mealPlanStateReducer
};
