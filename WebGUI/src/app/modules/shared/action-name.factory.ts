export const getActionNameCreator: (actionCategory: string) => (actionName: string) => string =
  actionCategory => actionName => `[${actionCategory}] ${actionName}`;
