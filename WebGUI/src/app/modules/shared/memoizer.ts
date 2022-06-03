import {shareReplay} from 'rxjs/operators';
import {Observable} from 'rxjs';

export function memoize<R>(fn: (t: any) => Observable<R>): (t: any) => Observable<R> {
  const cache = {};
  return (...args) => {
    const cacheKey = JSON.stringify(args);

    if (typeof cache[cacheKey] === 'undefined') {
      const result = fn(...args).pipe(shareReplay());
      cache[cacheKey] = result;
      return result;
    } else {
      return cache[cacheKey];
    }
  };
}
