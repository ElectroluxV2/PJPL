import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {firstValueFrom, map, Observable} from "rxjs";
import * as LZ from 'lz-string';

interface Event {
  from: number;
  to: number;
  room: string;
  location: string;
  groups: string[];
  color: string;
  additionalData: Record<string, string>;
}

interface Metadata {
  updated: number;
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
  public readonly subjects = new Map<number, Subject[]>();

  constructor(private readonly http: HttpClient) {
    void this.loadData();

    // console.log(new Date(new Date(1664969400 * 1000).toDateString()).valueOf())
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

        for (const group of groups) {
          const subjects: Subject[] = await firstValueFrom(this.loadSubjects(group.id));
          for (const subject of subjects) {
            const key = new Date(new Date(subject.from * 1000).toDateString()).valueOf(); // Date without time

            if (!this.subjects.has(key)) {
              this.subjects.set(key, []);
            }

            this.subjects.get(key)!.push(subject);
          }
        }
      }
    }

    const json = JSON.stringify(Array.from(this.subjects.entries()));
    console.log(json);
    const compressed = LZ.compress(json);
    console.log(compressed)
    const parsed = new Map(JSON.parse(json));
    console.log(parsed)
  }

  public loadSemesters(): Observable<Record<string, string>> {
    return this.getIndex('semesters');
  }

  public loadStudies(semesterId: string): Observable<Record<string, string>> {
    return this.getIndex(`${semesterId}/studies`);
  }

  public loadGroups(studyId: string): Observable<Record<string, string>> {
    return this.getIndex(`${studyId}/groups`);
  }

  public loadSubjects(groupId: string): Observable<Subject[]> {
    return this.get<Metadata & { subjects: Event[] }>(`${groupId}.json`).pipe(
      map(response => !response ? [] : response.subjects.map(event => ({
        ...event,
        updated: response.updated,
        study: response.study,
        name: response.name,
        semester: response.semester
      })))
    );
  }

  private getIndex(path: string): Observable<Record<string, string>> {
    return this.get(`${path}.json`);
  }

  private get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`${DataService.API}/${endpoint}`);
  }
}
