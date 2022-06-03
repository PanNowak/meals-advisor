export interface MealListControlsState {
  searchValue: string;
  pageIndex: number;
  pageSize: number;
}

export const initialState: MealListControlsState = {
  searchValue: '',
  pageIndex: 0,
  pageSize: 5
};
