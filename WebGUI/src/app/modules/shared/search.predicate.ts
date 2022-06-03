export function nameMatchesSearchValue(namedEntity: { name: string; },
                                       rawSearchValue: string): boolean {
  const searchValue = rawSearchValue.toLocaleLowerCase().trim();
  return namedEntity.name.toLocaleLowerCase().includes(searchValue);
}
