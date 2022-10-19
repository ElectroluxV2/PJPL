import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, map, Observable} from "rxjs";
import {MatOptionSelectionChange} from "@angular/material/core";

type Semester = Record<string, string>;
type Study = Record<string, string>;
type Group = Record<string, string>;

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

  private recordToMap(source: Observable<Record<string, string>>): Observable<Map<string, string>> {
    return source.pipe(
      map(record => new Map(Object.entries(record)))
    );
  }
}
