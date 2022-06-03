import {createReducer, on} from '@ngrx/store';
import {setMealPaginationInfoAction, setMealSearchValueAction} from 'app/modules/meal/store/meal-list-controls/meal-list-controls.actions';
import {initialState, MealListControlsState} from 'app/modules/meal/store/meal-list-controls/meal-list-controls.state';

export const mealListControlsStateReducer = createReducer(initialState,
  on(setMealSearchValueAction, setMealSearchValue),
  on(setMealPaginationInfoAction, setMealPaginationInfo)
);

function setMealSearchValue(state: MealListControlsState,
                            action: { searchValue: string; }): MealListControlsState {
  return {
    ...state,
    searchValue: action.searchValue
  };
}

function setMealPaginationInfo(state: MealListControlsState,
                               action: { pageIndex: number; pageSize: number; }): MealListControlsState {
  return {
    ...state,
    pageIndex: action.pageIndex,
    pageSize: action.pageSize
  };
}
