import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {SecondaryUnitInfo} from 'app/modules/product/models';
import {
  getAvailableSecondaryUnits,
  getChosenPrimaryUnit,
  getChosenSecondaryUnits,
  openAddSecondaryUnitDialogAction,
  removeSecondaryUnitAction
} from 'app/modules/product/store';
import {combineLatest, Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-secondary-unit-table',
  templateUrl: './secondary-unit-table.component.html'
})
export class SecondaryUnitTableComponent implements OnInit {

  isAddingSecondaryUnitDisallowed$: Observable<boolean>;

  displayedColumns = ['unitName', 'ratio', 'deleteButton'];
  secondaryUnits$: Observable<SecondaryUnitInfo[]>;

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.isAddingSecondaryUnitDisallowed$ = combineLatest(
      [this.isNoPrimaryUnitChosen(), this.isNoSecondaryUnitAvailable()])
      .pipe(map(([v1, v2]) => v1 || v2));

    this.secondaryUnits$ = this.store.select(getChosenSecondaryUnits);
  }

  addSecondaryUnit(): void {
    this.store.dispatch(openAddSecondaryUnitDialogAction());
  }

  deleteSecondaryUnit(secondaryUnit: SecondaryUnitInfo): void {
    this.store.dispatch(removeSecondaryUnitAction({secondaryUnitToRemove: secondaryUnit}));
  }

  private isNoPrimaryUnitChosen(): Observable<boolean> {
    return this.store.select(getChosenPrimaryUnit)
      .pipe(map(unit => !unit));
  }

  private isNoSecondaryUnitAvailable(): Observable<boolean> {
    return this.store.select(getAvailableSecondaryUnits)
      .pipe(map(secondaryUnits => secondaryUnits.length === 0));
  }
}
