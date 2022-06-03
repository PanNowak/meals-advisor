export class CachedIdsState {
  constructor(public allProductIds: number[] = [],
              public idsToRemove: number[] = []) {}

  public createNewState(newIds: Set<number>): CachedIdsState {
    const idsToRemove = this.allProductIds.filter(id => !newIds.has(id));
    return new CachedIdsState([...newIds], idsToRemove);
  }
}
