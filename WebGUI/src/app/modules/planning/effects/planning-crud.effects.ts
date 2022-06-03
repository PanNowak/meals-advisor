import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {PlanningService} from 'app/modules/planning/services/planning.service';
import {mealPlanGenerationFailure, mealPlanGenerationRequest, mealPlanGenerationSuccess} from 'app/modules/planning/store';
import {Observable, of} from 'rxjs';
import {catchError, map, switchMap} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class PlanningCrudEffects {

  generateMealPlan$ = createEffect(() =>
    this.actions$.pipe(
      ofType(mealPlanGenerationRequest),
      switchMap(action => this.handleMealPlanGeneration(action.firstDay, action.lastDay))
    )
  );

  constructor(private actions$: Actions,
              private planningService: PlanningService) {}

  private handleMealPlanGeneration(firstDay: number, lastDay: number): Observable<Action> {
    return this.planningService.draw(firstDay, lastDay)
      .pipe(
        map(plans => mealPlanGenerationSuccess({dayPlans: plans})),
        catchError(err => of(mealPlanGenerationFailure({error: err})))
      );
  }
}
