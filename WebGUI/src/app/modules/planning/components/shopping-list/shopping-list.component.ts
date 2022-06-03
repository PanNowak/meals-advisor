import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatTableDataSource} from '@angular/material/table';
import {GroceryItem} from 'app/modules/planning/models';

@Component({
  selector: 'app-shopping-list',
  templateUrl: './shopping-list.component.html',
  styleUrls: ['./shopping-list.component.css']
})
export class ShoppingListComponent {

  displayedColumns: string[] = ['product', 'amount', 'unit'];
  groceryItems: MatTableDataSource<GroceryItem>;

  constructor(@Inject(MAT_DIALOG_DATA) public data: GroceryItem[]) {
    this.groceryItems = new MatTableDataSource<GroceryItem>(data);
  }
}
