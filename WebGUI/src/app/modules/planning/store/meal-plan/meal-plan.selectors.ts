import {createSelector} from '@ngrx/store';
import {mealPlanAdapter} from 'app/modules/planning/store/meal-plan/meal-plan.state';
import {getPlanningState} from 'app/modules/planning/store/planning.selectors';

export const getMealPlanState = createSelector(getPlanningState,
  planningState => planningState.mealPlanState);

export const {selectAll: getDayPlans} = mealPlanAdapter.getSelectors(getMealPlanState);

export const isMealPlanGenerated = createSelector(getDayPlans,
   dayPlans => dayPlans.length > 0);
