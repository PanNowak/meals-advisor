import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {EffectsModule} from '@ngrx/effects';
import {LoadErrorDialogComponent} from 'app/modules/result-handling/components';
import {resultHandlingEffects} from 'app/modules/result-handling/effects';

@NgModule({
  declarations: [
    LoadErrorDialogComponent
  ],
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatSnackBarModule,
    EffectsModule.forFeature(resultHandlingEffects)
  ]
})
export class ResultHandlingModule {}
