import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {showLoadErrorDialog} from 'app/modules/result-handling/store';
import {initializeUnitListCachingAction, setUnitListAction, unitListFetchFailure, unitListFetchSuccess} from 'app/modules/unit/store';
import {concatMap, map} from 'rxjs/operators';

@Injectable()
export class UnitResultEffects {

  handleUnitListFetchSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(unitListFetchSuccess),
      concatMap(action => [
        setUnitListAction({units: action.units}),
        initializeUnitListCachingAction({unitListToCache: action.units})
      ])
    )
  );

  handleUnitListFetchFailure$ = createEffect(() =>
    this.actions$.pipe(
      ofType(unitListFetchFailure),
      map(action => showLoadErrorDialog({error: action.error}))
    )
  );

  constructor(private actions$: Actions) {}
}
