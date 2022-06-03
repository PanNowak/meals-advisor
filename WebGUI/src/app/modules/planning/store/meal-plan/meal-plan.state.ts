import {createEntityAdapter, EntityState} from '@ngrx/entity';
import {DayPlan} from 'app/modules/planning/models';

export const mealPlanAdapter =
  createEntityAdapter<DayPlan>({selectId: dayPlan => dayPlan.dayOfWeek});

export interface MealPlanState extends EntityState<DayPlan> {}

export const initialState: MealPlanState = mealPlanAdapter.getInitialState();
