import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Unit} from 'app/modules/unit/models';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UnitService {

  private readonly unitUri = 'units';

  constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<Unit[]> {
    return this.httpClient.get<Unit[]>(this.unitUri);
  }
}
