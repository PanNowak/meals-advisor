import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {mealPlanGenerationFailure, mealPlanGenerationSuccess, setMealPlanAction} from 'app/modules/planning/store';
import {showLoadErrorDialog} from 'app/modules/result-handling/store';
import {map} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class PlanningResultEffects {

  handleMealPlanGenerationSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealPlanGenerationSuccess),
      map(action => setMealPlanAction({dayPlans: action.dayPlans}))
    )
  );

  handleMealPlanGenerationFailure$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealPlanGenerationFailure),
      map(action => showLoadErrorDialog({error: action.error}))
    )
  );

  constructor(private actions$: Actions) {}
}
