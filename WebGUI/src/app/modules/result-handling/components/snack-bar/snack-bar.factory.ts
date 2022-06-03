import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import {HttpErrorResponse} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SnackBarFactory {

  constructor(private snackBar: MatSnackBar) {}

  public showSuccessSnackBar(message: string): void {
    this.snackBar.open(message, '', {duration: 5000});
  }

  public showErrorSnackBar(error: any): void {
    if (error instanceof HttpErrorResponse && error.error.localizedMessage) {
      this.snackBar.open(error.error.localizedMessage, `Kod: ${error.error.status}`);
    } else {
      console.log(error);
      this.snackBar.open('Wystąpił nieoczekiwany błąd', '', {duration: 5000});
    }
  }
}
