import {Injectable} from '@angular/core';
import {AbstractControl, AsyncValidator, AsyncValidatorFn, ValidationErrors} from '@angular/forms';
import {Store} from '@ngrx/store';
import {Observable, of} from 'rxjs';
import {concatMap, first, map} from 'rxjs/operators';
import {AppState} from 'app/store/app.state';
import {getAllProductNames, getLoadedProductName, productListLoadRequest} from 'app/modules/product/store';

const validationPassedPlaceholder = null;

@Injectable({
  providedIn: 'root'
})
export class ProductValidatorsFactory {

  constructor(private store: Store<AppState>) {}

  uniqueName(): AsyncValidatorFn {
    this.store.dispatch(productListLoadRequest());
    const validator = new UniqueNameValidator(this.store);
    return control => validator.validate(control);
  }
}

class UniqueNameValidator implements AsyncValidator {

  constructor(private store: Store<AppState>) {}

  validate(control: AbstractControl): Observable<ValidationErrors> {
    return this.store.select(getLoadedProductName).pipe(
      concatMap(loadedProductName => this.validateIsUnique(control.value, loadedProductName)),
      first()
    );
  }

  private validateIsUnique(currentProductName: string,
                           loadedProductName: string): Observable<ValidationErrors> {
    if (currentProductName === loadedProductName) {
      return of(validationPassedPlaceholder);
    } else {
      return this.validateNotDuplicate(currentProductName);
    }
  }

  private validateNotDuplicate(currentProductName: string): Observable<ValidationErrors> {
    return this.store.select(getAllProductNames).pipe(
      map(allProductNames =>
        allProductNames.includes(currentProductName) ? {duplicate: true} : validationPassedPlaceholder
      )
    );
  }
}
