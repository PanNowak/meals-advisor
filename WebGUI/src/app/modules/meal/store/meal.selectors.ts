import {createFeatureSelector} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {MealState} from 'app/modules/meal/store/meal.state';

export const mealStateFeatureKey = 'mealState';

export const getMealState = createFeatureSelector<AppState, MealState>(mealStateFeatureKey);
