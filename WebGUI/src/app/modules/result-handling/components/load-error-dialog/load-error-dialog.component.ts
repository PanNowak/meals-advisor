import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-load-error-dialog',
  templateUrl: './load-error-dialog.component.html',
})
export class LoadErrorDialogComponent implements OnInit {

  errorMessage: string;

  constructor(@Inject(MAT_DIALOG_DATA) public error: Error) { }

  ngOnInit(): void {
    this.setMessage(this.error);
  }

  reloadPage(): void {
    window.location.reload();
  }

  private setMessage(error: Error): void {
    if (error instanceof HttpErrorResponse && error.error.localizedMessage) {
      const knownError = error.error;
      this.errorMessage = `${knownError.localizedMessage} (Kod: ${knownError.status})`;
    } else {
      this.errorMessage = 'Wystąpił nieoczekiwany błąd';
    }
  }
}
