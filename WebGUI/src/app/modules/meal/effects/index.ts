import {MealCacheEffects} from 'app/modules/meal/effects/meal-cache.effects';
import {MealCrudEffects} from 'app/modules/meal/effects/meal-crud.effects';
import {MealDialogEffects} from 'app/modules/meal/effects/meal-dialog.effects';
import {MealResultEffects} from 'app/modules/meal/effects/meal-result.effects';

export const mealEffects = [
  MealCacheEffects,
  MealCrudEffects,
  MealDialogEffects,
  MealResultEffects
];
