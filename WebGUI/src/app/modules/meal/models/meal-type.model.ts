export interface MealType {
  id: number;
  name: string;
}

export function areMealTypesEqual(mealType1: MealType, mealType2: MealType): boolean {
  if (mealType1 === mealType2) {
    return true;
  }

  return mealType1 && mealType2 && mealType1.name === mealType2.name;
}
