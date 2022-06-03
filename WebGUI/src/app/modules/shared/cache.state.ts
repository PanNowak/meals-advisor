export interface CacheState {
  isInvalidated: boolean;
}

export const initialCacheState: CacheState = {
  isInvalidated: true
};
