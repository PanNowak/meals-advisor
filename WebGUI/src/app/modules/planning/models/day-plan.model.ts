import {MealSummary} from 'app/modules/meal/models';

export interface DayPlan {
  dayOfWeek: number;
  mealsByType: {
    [type: string]: MealSummary;
  };
}
