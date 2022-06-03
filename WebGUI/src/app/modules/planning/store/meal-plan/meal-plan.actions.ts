import {createAction, props} from '@ngrx/store';
import {DayPlan} from 'app/modules/planning/models';
import {getActionNameCreator} from 'app/modules/shared/action-name.factory';

const $n = getActionNameCreator('Meal Plan');

export const mealPlanGenerationRequest = createAction(
  $n('Generation Request'), props<{ firstDay: number; lastDay: number; }>()
);

export const mealPlanGenerationSuccess = createAction(
  $n('Generation Success'), props<{ dayPlans: DayPlan[]; }>()
);

export const mealPlanGenerationFailure = createAction(
  $n('Generation Failure'), props<{ error: Error; }>()
);

export const setMealPlanAction = createAction(
  $n('Set Plan'), props<{ dayPlans: DayPlan[]; }>()
);
