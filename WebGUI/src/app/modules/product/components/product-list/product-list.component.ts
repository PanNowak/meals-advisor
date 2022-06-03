import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {Store} from '@ngrx/store';
import {AppState} from 'app/store/app.state';
import {ProductSummary} from 'app/modules/product/models';
import {
  getProductList,
  getProductListControlsState,
  navigateToProductDetailsViewAction,
  openAddProductDialogAction,
  openDeleteProductDialogAction,
  setProductPaginationInfoAction,
  setProductSearchValueAction
} from 'app/modules/product/store';
import {ProductListControlsState} from 'app/modules/product/store/product-list-controls/product-list-controls.state';
import {ObservableBasedDataSource} from 'app/modules/shared/observable-based-data-source';
import {nameMatchesSearchValue} from 'app/modules/shared/search.predicate';
import {Subscription} from 'rxjs';
import {concatMapTo, filter, first, map} from 'rxjs/operators';

@Component({
  selector: 'app-product',
  templateUrl: './product-list.component.html'
})
export class ProductListComponent implements OnInit, OnDestroy {

  readonly displayedColumns = ['productName', 'deleteButton'];
  readonly products = new ObservableBasedDataSource<ProductSummary>();

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  private pageEventsSubscription = new Subscription();

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.setUpPaginator();
    this.setUpDataSourceLogic();
    this.loadPreviousControlsState();
  }

  ngOnDestroy(): void {
    this.pageEventsSubscription.unsubscribe();
  }

  showProductDetails(productId: number): void {
    this.store.dispatch(navigateToProductDetailsViewAction({requestedProductId: productId}));
  }

  addProduct(): void {
    this.store.dispatch(openAddProductDialogAction());
  }

  deleteProduct(product: ProductSummary): void {
    this.store.dispatch(openDeleteProductDialogAction({productToDelete: product}));
  }

  get searchValue(): string {
    return this.products.filter;
  }

  set searchValue(value: string) {
    this.store.dispatch(setProductSearchValueAction({searchValue: value}));
    this.products.filter = value;
  }

  private setUpPaginator(): void {
    const paginatorIntl = this.paginator._intl;
    paginatorIntl.itemsPerPageLabel = 'Produktów na stronę';
    paginatorIntl.firstPageLabel = 'Pierwsza strona';
    paginatorIntl.previousPageLabel = 'Poprzednia strona';
    paginatorIntl.nextPageLabel = 'Następna strona';
    paginatorIntl.lastPageLabel = 'Ostatnia strona';
  }

  private setUpDataSourceLogic(): void {
    this.products.observableData = this.store.select(getProductList);
    this.products.filterPredicate = nameMatchesSearchValue;
    this.products.paginator = this.paginator;
  }

  private loadPreviousControlsState(): void {
    this.store.select(getProductList).pipe(
      filter(products => products.length > 0),
      concatMapTo(this.store.select(getProductListControlsState)),
      first()
    ).subscribe(controlsState => {
      this.updateControls(controlsState);
      this.observePaginationEvents();
    });
  }

  private updateControls(controlsState: ProductListControlsState): void {
    this.products.filter = controlsState.searchValue;
    this.paginator.pageIndex = controlsState.pageIndex;
    this.paginator.pageSize = controlsState.pageSize;
    this.paginator.page.next({
      pageIndex: this.paginator.pageIndex,
      pageSize: this.paginator.pageSize,
      length: this.paginator.length
    });
  }

  private observePaginationEvents(): void {
    this.pageEventsSubscription = this.paginator.page.pipe(
      map(pageEvent => setProductPaginationInfoAction(
        {pageIndex: pageEvent.pageIndex, pageSize: pageEvent.pageSize}))
    ).subscribe(action => this.store.dispatch(action));
  }
}
