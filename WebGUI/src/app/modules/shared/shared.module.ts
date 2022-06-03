import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {StoreModule} from '@ngrx/store';
import {ConfirmationDialogComponent} from 'app/modules/shared/confirmation-dialog/confirmation-dialog.component';
import {backgroundTaskCountStateReducer} from 'app/modules/shared/store/background-task-count/background-task-count.reducer';
import {backgroundTaskCountStateFeatureKey} from 'app/modules/shared/store/background-task-count/background-task-count.selectors';

@NgModule({
  declarations: [
    ConfirmationDialogComponent,
    // LoadErrorDialogComponent
  ],
  imports: [
    MatDialogModule,
    MatButtonModule,
    // MatSnackBarModule,
    // RouterModule,
    StoreModule.forFeature(backgroundTaskCountStateFeatureKey, backgroundTaskCountStateReducer),
    // EffectsModule.forFeature([SnackBarEffects])
  ]
})
export class SharedModule { }
