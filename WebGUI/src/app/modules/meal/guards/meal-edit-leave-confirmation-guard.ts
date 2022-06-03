import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Selector, Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {isCurrentMealIdenticalToLoaded} from 'app/modules/meal/store';
import {DialogFactory} from 'app/modules/shared/dialog.factory';
import {LeaveConfirmationGuard} from 'app/modules/shared/leave-confirmation.guard';

@Injectable({
  providedIn: 'root'
})
export class MealEditLeaveConfirmationGuard extends LeaveConfirmationGuard {

  constructor(store: Store<AppState>,
              router: Router,
              dialogFactory: DialogFactory) {
    super(store, router, dialogFactory);
  }

  protected noChangesSelector(): Selector<AppState, boolean> {
    return isCurrentMealIdenticalToLoaded;
  }
}
