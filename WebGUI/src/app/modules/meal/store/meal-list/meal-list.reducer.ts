import {createReducer, on} from '@ngrx/store';
import {MealSummary} from 'app/modules/meal/models';
import {clearMealListStateAction, setMealListAction} from 'app/modules/meal/store/meal-list/meal-list.actions';
import {initialState, mealListAdapter, MealListState} from 'app/modules/meal/store/meal-list/meal-list.state';

export const mealListStateReducer = createReducer(initialState,
  on(clearMealListStateAction, () => initialState),
  on(setMealListAction, setMealList)
);

function setMealList(state: MealListState,
                     action: { meals: MealSummary[]; }): MealListState {
  return mealListAdapter.setAll(action.meals, state);
}
