import {Comparer} from '@ngrx/entity';

const collator = new Intl.Collator('pl');

export function comparingStrings<T>(keyExtractor: (t: T) => string): Comparer<T> {
  return (t1, t2) => collator.compare(keyExtractor(t1), keyExtractor(t2));
}

export function comparingNumbers<T>(keyExtractor: (t: T) => number): Comparer<T> {
  return (t1, t2) => keyExtractor(t1) - keyExtractor(t2);
}

export function comparingDates<T>(keyExtractor: (t: T) => Date): Comparer<T> {
  return comparingNumbers(t => keyExtractor(t).getTime());
}
