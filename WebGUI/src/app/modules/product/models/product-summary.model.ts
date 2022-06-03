export interface ProductSummary {
  id: number;
  name: string;
}

export function areProductSummariesEqual(product1: ProductSummary,
                                         product2: ProductSummary): boolean {
  if (product1 === product2) {
    return true;
  }

  return product1 && product2 && product1.name === product2.name;
}
