import {Action} from '@ngrx/store';

export class RoutingButtonInfo {
  label: string;
  imageLink: string;
  navigationAction: Action;

  constructor(label: string, imageName: string, navigationAction: Action) {
    this.label = label;
    this.imageLink = `assets/images/${imageName}`;
    this.navigationAction = navigationAction;
  }
}
