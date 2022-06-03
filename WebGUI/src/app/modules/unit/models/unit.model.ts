export interface Unit {
  id: number;
  name: string;
}

export function areUnitsEqual(unit1: Unit, unit2: Unit): boolean {
  if (unit1 === unit2) {
    return true;
  }

  return unit1 && unit2 && unit1.name === unit2.name;
}
