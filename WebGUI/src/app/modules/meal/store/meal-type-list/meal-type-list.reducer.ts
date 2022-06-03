import {createReducer, on} from '@ngrx/store';
import {MealType} from 'app/modules/meal/models';
import {clearMealTypeListStateAction, setMealTypeListAction} from 'app/modules/meal/store/meal-type-list/meal-type-list.actions';
import {initialState, mealTypeListAdapter, MealTypeListState} from 'app/modules/meal/store/meal-type-list/meal-type-list.state';

export const mealTypeListStateReducer = createReducer(initialState,
  on(clearMealTypeListStateAction, () => initialState),
  on(setMealTypeListAction, setMealTypeList)
);

function setMealTypeList(currentState: MealTypeListState,
                         action: { mealTypes: MealType[] }): MealTypeListState {
  return mealTypeListAdapter.setAll(action.mealTypes, currentState);
}
