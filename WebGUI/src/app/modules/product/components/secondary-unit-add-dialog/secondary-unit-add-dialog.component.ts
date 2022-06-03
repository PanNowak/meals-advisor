import {Component, Inject} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {SecondaryUnitInfo} from 'app/modules/product/models';
import {areUnitsEqual, Unit} from 'app/modules/unit/models';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-secondary-unit-add-dialog',
  templateUrl: './secondary-unit-add-dialog.component.html'
})
export class SecondaryUnitAddDialogComponent {

  selectedControl = new FormControl('', [
    Validators.required,
  ]);

  ratioControl = new FormControl('', [
    Validators.required,
    Validators.pattern('^([0-9]?|([1-9][0-9]*))(\\.[0-9]+)?$'),
  ]);

  constructor(@Inject(MAT_DIALOG_DATA) public availableSecondaryUnits$: Observable<Unit[]>,
              public dialogRef: MatDialogRef<SecondaryUnitAddDialogComponent>) {
  }

  addSecondaryUnit(): void {
    const secondaryUnitInfo = this.createSecondaryUnitInfo();
    this.dialogRef.close(secondaryUnitInfo);
  }

  isAnyInputIncorrect(): boolean {
    return this.selectedControl.hasError('required') ||
      this.ratioControl.hasError('required') ||
      this.ratioControl.hasError('pattern');
  }

  areUnitsEqual(unit1: Unit, unit2: Unit): boolean {
    return areUnitsEqual(unit1, unit2);
  }

  private createSecondaryUnitInfo(): SecondaryUnitInfo {
    return {
      id: null,
      unit: this.selectedControl.value,
      toPrimaryUnitRatio: parseFloat(this.ratioControl.value)
    };
  }
}
