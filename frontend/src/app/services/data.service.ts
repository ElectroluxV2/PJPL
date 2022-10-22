import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {firstValueFrom, Observable} from "rxjs";

interface Event {
  from: Date;
  to: Date;
  room: string;
  location: string;
  groups: string[];
  color: string;
  additionalData: Record<string, string>;
}

interface Metadata {
  updated: Date;
  study: string;
  name: string;
  semester: string;
}

export type Subject = Event & Metadata;

export interface IndexItem {
  name: string;
  id: string;
}

export type Semester = IndexItem;
export type Study = IndexItem;
export type Group = IndexItem;

export interface Grouped<T> {
  name: string;
  items: T[];
}


@Injectable({
  providedIn: 'root'
})
export class DataService {
  private static readonly API = "//s2.budziszm.pl/data";
  public readonly availableSemesters: Semester[] = [];
  public readonly selectedSemestersIds = new Set<string>();
  public readonly availableStudies: Grouped<Study>[] = [];
  public readonly selectedStudiesIds = new Set<string>();
  public readonly availableGroups: Grouped<Group>[] = [];
  public readonly selectedGroupsIds = new Set<string>();

  constructor(private readonly http: HttpClient) {
    void this.loadData();
  }

  private async loadData(): Promise<void> {
    const semesters: Semester[] = Object
      .entries(await firstValueFrom(this.loadSemesters()))
      .map(([name, id]) => ({name, id}));

    this.availableSemesters.push(...semesters);

    for (const semester of semesters) {
      const studies: Study[] = Object
        .entries(await firstValueFrom(this.loadStudies(semester.id)))
        .map(([name, id]) => ({name, id: `${semester.id}/${id}`}));

      this.availableStudies.push({
        name: semester.name,
        items: studies
      });

      for (const study of studies) {
        const groups: Group[] = Object
          .entries(await firstValueFrom(this.loadGroups(study.id)))
          .map(([name, id]) => ({name, id: `${study.id}/${id}`}));

        this.availableGroups.push({
          name: `${semester.name} - ${study.name}`,
          items: groups
        });
      }
    }
  }

  public loadSemesters(): Observable<Record<string, string>> {
    return this.getIndex('semesters');
  }

  public loadStudies(semester: string): Observable<Record<string, string>> {
    return this.getIndex(`${semester}/studies`);
  }

  public loadGroups(semesterWithStudy: string): Observable<Record<string, string>> {
    return this.getIndex(`${semesterWithStudy}/groups`);
  }

  private getIndex(path: string): Observable<Record<string, string>> {
    return this.get<Record<string, string>>(`${path}.json`);
  }

  private get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`${DataService.API}/${endpoint}`);
  }
}
