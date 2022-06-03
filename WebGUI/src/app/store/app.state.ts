import {MealState} from 'app/modules/meal/store';
import {PlanningState} from 'app/modules/planning/store';
import {ProductState} from 'app/modules/product/store';
import {UnitState} from 'app/modules/unit/store';
import {BackgroundTaskCountState} from 'app/modules/shared/store/background-task-count/background-task-count.state';

export interface AppState {
  planningState: PlanningState;
  mealState: MealState;
  productState: ProductState;
  unitState: UnitState;
  backgroundTaskCountState: BackgroundTaskCountState;
}
