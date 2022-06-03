import {AbstractControlOptions, AsyncValidatorFn, FormControl, ValidatorFn} from '@angular/forms';
import {BehaviorSubject, Observable} from 'rxjs';
import {debounceTime, distinctUntilChanged, filter, map, startWith} from 'rxjs/operators';

export function selectedValueObservable(selectControl: FormControl): Observable<any> {
  return selectControl.valueChanges
    .pipe(filter(() => selectControl.dirty));
}

export function textInputObservable(inputControl: FormControl): Observable<any> {
  return inputControl.valueChanges
    .pipe(
      filter(() => inputControl.dirty),
      debounceTime(100),
      distinctUntilChanged()
    );
}

export function errorObservable(control: FormControl, errorCode: string): Observable<boolean> {
  return control.statusChanges
    .pipe(
      startWith(control.hasError(errorCode)),
      map(() => control.hasError(errorCode))
    );
}

export class SelectControl<T> extends FormControl {

  private readonly selectedValueSubject: BehaviorSubject<T>;

  constructor(formState?: any, validatorOrOpts?: ValidatorFn | ValidatorFn[] | AbstractControlOptions | null,
              asyncValidator?: AsyncValidatorFn | AsyncValidatorFn[] | null) {
    super(formState, validatorOrOpts, asyncValidator);
    this.selectedValueSubject = new BehaviorSubject(null);
    this.valueChanges
      .pipe(filter(() => this.dirty))
      .subscribe(this.selectedValueSubject);
  }

  observeSelectedValue(next: (value: T) => void): void {
    this.selectedValueSubject.subscribe(next);
  }

  getLastEmittedValue(): T {
    return this.selectedValueSubject.getValue();
  }

  cleanUp(): void {
    this.selectedValueSubject.complete();
    this.selectedValueSubject.unsubscribe();
  }
}
