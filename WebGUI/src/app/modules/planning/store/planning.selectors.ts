import {createFeatureSelector} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {PlanningState} from 'app/modules/planning/store/planning.state';

export const planningStateFeatureKey = 'planningState';

export const getPlanningState = createFeatureSelector<AppState, PlanningState>(planningStateFeatureKey);
