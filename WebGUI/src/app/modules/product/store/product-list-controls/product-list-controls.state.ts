export interface ProductListControlsState {
  searchValue: string;
  pageIndex: number;
  pageSize: number;
}

export const initialState: ProductListControlsState = {
  searchValue: '',
  pageIndex: 0,
  pageSize: 5
};
