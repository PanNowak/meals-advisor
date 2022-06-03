import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {UnitService} from 'app/modules/unit/services/unit.service';
import {
  getCachedUnitList,
  getUnitCacheState,
  setUnitListAction,
  unitListFetchFailure,
  unitListFetchRequest,
  unitListFetchSuccess,
  unitListLoadRequest
} from 'app/modules/unit/store';
import {Observable, of} from 'rxjs';
import {catchError, map, switchMap, take} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class UnitCrudEffects {

  loadUnitList$ = createEffect(() =>
    this.actions$.pipe(
      ofType(unitListLoadRequest),
      switchMap(() => this.handleUnitListLoad())
    )
  );

  fetchUnitList$ = createEffect(() =>
    this.actions$.pipe(
      ofType(unitListFetchRequest),
      switchMap(() => this.handleUnitListFetch())
    )
  );

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private unitService: UnitService) {}

  private handleUnitListLoad(): Observable<Action> {
    return this.store.select(getUnitCacheState).pipe(
      take(1),
      map(unitCacheState => {
        if (unitCacheState.isInvalidated) {
          return unitListFetchRequest();
        } else {
          const cachedUnitList = getCachedUnitList(unitCacheState);
          return setUnitListAction({units: cachedUnitList});
        }
      })
    );
  }

  private handleUnitListFetch(): Observable<Action> {
    return this.unitService.getAll()
      .pipe(
        map(unitList => unitListFetchSuccess({units: unitList})),
        catchError(err => of(unitListFetchFailure({error: err})))
      );
  }
}
