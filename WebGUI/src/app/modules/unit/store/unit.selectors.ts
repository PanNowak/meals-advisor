import {createFeatureSelector} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {UnitState} from 'app/modules/unit/store/unit.state';

export const unitStateFeatureKey = 'unitState';

export const getUnitState = createFeatureSelector<AppState, UnitState>(unitStateFeatureKey);
