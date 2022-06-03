import {HttpErrorResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {showErrorSnackBar, showSuccessSnackBar} from 'app/modules/result-handling/store';
import {map} from 'rxjs/operators';

// noinspection JSUnusedGlobalSymbols
@Injectable()
export class SnackBarEffects {

  showSuccessSnackBar$ = createEffect(() =>
    this.actions$.pipe(
      ofType(showSuccessSnackBar),
      map(action => this.showSuccessSnackBar(action.message))
    ), {dispatch: false});

  showErrorSnackBar$ = createEffect(() =>
    this.actions$.pipe(
      ofType(showErrorSnackBar),
      map(action => this.showErrorSnackBar(action.error))
    ), {dispatch: false});

  constructor(private actions$: Actions,
              private snackBar: MatSnackBar) {}

  private showSuccessSnackBar(message: string): void {
    this.snackBar.open(message, '', {duration: 2000});
  }

  private showErrorSnackBar(error: Error): void {
    console.log(JSON.stringify(error));
    if (error instanceof HttpErrorResponse && error.error.localizedMessage) {
      this.snackBar.open(error.error.localizedMessage, `Kod: ${error.error.status}`, {duration: 5000});
    } else {
      this.snackBar.open('Wystąpił nieoczekiwany błąd', '', {duration: 5000});
    }
  }
}
