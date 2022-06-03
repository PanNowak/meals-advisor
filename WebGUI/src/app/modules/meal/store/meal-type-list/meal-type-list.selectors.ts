import {createSelector} from '@ngrx/store';
import {mealTypeListAdapter} from 'app/modules/meal/store/meal-type-list/meal-type-list.state';
import {getMealState} from 'app/modules/meal/store/meal.selectors';

export const getMealTypeListState = createSelector(getMealState,
  mealState => mealState.mealTypeListState);

export const {selectAll: getMealTypeList} = mealTypeListAdapter.getSelectors(getMealTypeListState);

export const getMealTypeNames = createSelector(getMealTypeList,
  mealTypes => mealTypes.map(mealType => mealType.name));
