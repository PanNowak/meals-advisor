import {CanDeactivate, Router} from '@angular/router';
import {Selector, Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {concatMap, defaultIfEmpty, filter, first} from 'rxjs/operators';
import {AppState} from 'app/store/app.state';
import {DialogFactory} from 'app/modules/shared/dialog.factory';

export abstract class LeaveConfirmationGuard implements CanDeactivate<any> {

  protected constructor(private store: Store<AppState>,
                        private router: Router,
                        private dialogFactory: DialogFactory) {}

  canDeactivate(): Observable<boolean> | boolean {
    if (this.areChangesSaved()) {
      return true;
    }

    return this.confirmPageDeactivation();
  }

  protected abstract noChangesSelector(): Selector<AppState, boolean>;

  private areChangesSaved(): boolean {
    return this.router.getCurrentNavigation().extras.state?.isStateSaved;
  }

  private confirmPageDeactivation(): Observable<boolean> {
    return this.store.select(this.noChangesSelector())
      .pipe(
        first(),
        filter(isUnchanged => !isUnchanged),
        concatMap(() => this.dialogFactory.showConfirmationDialog(
          `Czy jesteś pewien, że chcesz opuścić stronę? Stracisz niezapisane zmiany`)),
        defaultIfEmpty(true)
      );
  }
}
