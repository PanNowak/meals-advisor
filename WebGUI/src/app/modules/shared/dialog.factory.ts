import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {LoadErrorDialogComponent} from 'app/modules/result-handling/components';
import {ConfirmationDialogComponent} from 'app/modules/shared/confirmation-dialog/confirmation-dialog.component';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DialogFactory {

  constructor(private dialog: MatDialog) {}

  public showConfirmationDialog(message: string): Observable<boolean> {
    return this.dialog.open(ConfirmationDialogComponent, {data: message}).afterClosed();
  }

  public showErrorDialog(error: Error): void {
    this.dialog.open(LoadErrorDialogComponent, {
      data: error,
      width: '50%',
      disableClose: true
    });
  }
}
