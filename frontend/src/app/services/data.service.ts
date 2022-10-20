import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private static readonly API = "//s2.budziszm.pl/data";

  constructor(private readonly http: HttpClient) {

  }

  public loadSemesters(): Observable<Record<string, string>> {
    return this
      .get<Record<string, string>>('semesters.json');
  }

  private get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`${DataService.API}/${endpoint}`);
  }
}
