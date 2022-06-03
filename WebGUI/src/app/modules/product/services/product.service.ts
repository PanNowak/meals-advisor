import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Product, ProductSummary} from 'app/modules/product/models';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private readonly productUri = 'products';

  constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<ProductSummary[]> {
    return this.httpClient.get<ProductSummary[]>(this.productUri);
  }

  public getById(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(`${this.productUri}/${productId}`);
  }

  public create(product: Product): Observable<Product> {
    return this.httpClient.post<Product>(this.productUri, product);
  }

  public update(productId: number, product: Product): Observable<Product> {
    return this.httpClient.put<Product>(`${this.productUri}/${productId}`, product);
  }

  public deleteById(productId: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.productUri}/${productId}`);
  }
}
