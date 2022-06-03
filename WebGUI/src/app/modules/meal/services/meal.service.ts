import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Meal, MealSummary, MealType} from 'app/modules/meal/models';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MealService {

  private readonly mealUri = 'meals';

  constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<MealSummary[]> {
    return this.httpClient.get<MealSummary[]>(this.mealUri);
  }

  public getAllTypes(): Observable<MealType[]> {
    return this.httpClient.get<MealType[]>(`${this.mealUri}/types`);
  }

  public getById(mealId: number): Observable<Meal> {
    return this.httpClient.get<Meal>(`${this.mealUri}/${mealId}`);
  }

  public create(meal: Meal): Observable<Meal> {
    return this.httpClient.post<Meal>(this.mealUri, meal);
  }

  public update(mealId: number, meal: Meal): Observable<Meal> {
    return this.httpClient.put<Meal>(`${this.mealUri}/${mealId}`, meal);
  }

  public deleteById(mealId: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.mealUri}/${mealId}`);
  }
}
