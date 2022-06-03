import {areUnitsEqual, Unit} from 'app/modules/unit/models';

export interface SecondaryUnitInfo {
  id: number;
  unit: Unit;
  toPrimaryUnitRatio: number;
}

export function areSecondaryUnitsEqual(secondaryUnit1: SecondaryUnitInfo,
                                       secondaryUnit2: SecondaryUnitInfo): boolean {
  if (secondaryUnit1 === secondaryUnit2) {
    return true;
  }

  return secondaryUnit1 && secondaryUnit2 &&
    secondaryUnit1.toPrimaryUnitRatio === secondaryUnit2.toPrimaryUnitRatio &&
    areUnitsEqual(secondaryUnit1.unit, secondaryUnit2.unit);
}
