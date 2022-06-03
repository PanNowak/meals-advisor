import {createFeatureSelector, createSelector} from '@ngrx/store';
import {BackgroundTaskCountState} from 'app/modules/shared/store/background-task-count/background-task-count.state';
import {AppState} from 'app/store/app.state';

export const backgroundTaskCountStateFeatureKey = 'backgroundTaskCountState';

export const getBackgroundTaskCountState =
  createFeatureSelector<AppState, BackgroundTaskCountState>(backgroundTaskCountStateFeatureKey);

export const isAnyTaskRunning = createSelector(getBackgroundTaskCountState,
  taskCountState => taskCountState.numberOfCurrentlyRunningTasks > 0);
