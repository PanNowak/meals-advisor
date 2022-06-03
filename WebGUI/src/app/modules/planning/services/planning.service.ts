import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {DayPlan, GroceryItem} from 'app/modules/planning/models';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PlanningService {

  constructor(private httpClient: HttpClient) { }

  private static prepareDrawQueryParams(firstDay: number, lastDay: number): HttpParams {
    return new HttpParams()
      .set('first-day', String(firstDay))
      .set('last-day', String(lastDay));
  }

  private static concatIds(mealIds: number[]): string {
    return mealIds.map(String).reduce((a, b) => `${a},${b}`);
  }

  public draw(firstDay: number, lastDay: number): Observable<DayPlan[]> {
    const queryParams = PlanningService.prepareDrawQueryParams(firstDay, lastDay);
    return this.httpClient.get<DayPlan[]>('planning/draw', {params: queryParams});
  }

  public generateShoppingList(mealIds: number[]): Observable<GroceryItem[]> {
    const queryParams = new HttpParams().set('meal-id', PlanningService.concatIds(mealIds));
    return this.httpClient.get<GroceryItem[]>('planning/shopping-list', {params: queryParams});
  }
}
