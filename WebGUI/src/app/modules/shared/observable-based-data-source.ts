import {MatTableDataSource} from '@angular/material/table';
import {EMPTY, Observable, Subscription} from 'rxjs';

export class ObservableBasedDataSource<T> extends MatTableDataSource<T> {

  private dataSubscription: Subscription;

  constructor(observableData: Observable<T[]> = EMPTY) {
    super();
    this.dataSubscription = observableData.subscribe(data => this.data = data);
  }

  set observableData(observableData: Observable<T[]>) {
    this.dataSubscription.unsubscribe();
    this.dataSubscription = observableData.subscribe(data => this.data = data);
  }

  disconnect(): void {
    super.disconnect();
    this.dataSubscription.unsubscribe();
  }
}
