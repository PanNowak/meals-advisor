import {createReducer, on} from '@ngrx/store';
import {decrementBackgroundTaskCount, incrementBackgroundTaskCount} from 'app/modules/shared/store/background-task-count/background-task-count.actions';
import {BackgroundTaskCountState, initialState} from 'app/modules/shared/store/background-task-count/background-task-count.state';

export const backgroundTaskCountStateReducer = createReducer(initialState,
  on(incrementBackgroundTaskCount, incrementTaskCount),
  on(decrementBackgroundTaskCount, decrementTaskCount)
);

function incrementTaskCount(currentState: BackgroundTaskCountState): BackgroundTaskCountState {
  return {
    ...currentState,
    numberOfCurrentlyRunningTasks: currentState.numberOfCurrentlyRunningTasks + 1
  };
}

function decrementTaskCount(currentState: BackgroundTaskCountState): BackgroundTaskCountState {
  return {
    ...currentState,
    numberOfCurrentlyRunningTasks: currentState.numberOfCurrentlyRunningTasks - 1
  };
}
