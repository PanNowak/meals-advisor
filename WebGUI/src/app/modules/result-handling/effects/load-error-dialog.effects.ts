import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {LoadErrorDialogComponent} from 'app/modules/result-handling/components';
import {showLoadErrorDialog} from 'app/modules/result-handling/store';
import {isAnyTaskRunning} from 'app/modules/shared/store/background-task-count/background-task-count.selectors';
import {combineLatest, Observable} from 'rxjs';
import {delay, exhaustMap, filter} from 'rxjs/operators';

@Injectable()
export class LoadErrorDialogEffects {

  showLoadErrorDialog$ = createEffect(() =>
    combineLatest([
      this.actions$.pipe(ofType(showLoadErrorDialog), delay(0)),
      this.store.select(isAnyTaskRunning)])
      .pipe(
        filter(([, b]) => !b),
        exhaustMap(([action, ]) => this.showErrorDialog(action.error))
      ), {dispatch: false});

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private dialog: MatDialog) {}

  private showErrorDialog(error: Error): Observable<any> {
    return this.dialog.open(LoadErrorDialogComponent, {
      data: error,
      width: '50%',
      disableClose: true
    }).afterClosed();
  }
}
