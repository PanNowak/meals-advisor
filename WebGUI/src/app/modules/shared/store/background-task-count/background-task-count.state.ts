export interface BackgroundTaskCountState {
  numberOfCurrentlyRunningTasks: number;
}

export const initialState: BackgroundTaskCountState = {
  numberOfCurrentlyRunningTasks: 0
};
