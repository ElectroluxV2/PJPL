import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, firstValueFrom, map, Observable} from "rxjs";
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
  private readonly subjects = new Map<number, Subject[]>();
  public readonly subjects$ = new BehaviorSubject<Map<number, Subject[]>>(new Map());
  public begin: number = Number.MAX_VALUE;
  public end: number = 0;

  constructor(private readonly http: HttpClient) {
    // this.sync().then(() => );
    this.load();

    // console.log(new Date(new Date(1664969400 * 1000).toDateString()).valueOf())
  }

  private load(): void {
    console.time('load');

    const cachedGroups = Object.keys(localStorage).filter(key => key.includes('G-'));

    for (const groupKey of cachedGroups) {
      const compressed = localStorage.getItem(groupKey)!;
      const json = LZ.decompress(compressed)!;
      const subjects: Subject[] = JSON.parse(json);

      for (const subject of subjects) {
        const from = subject.from * 1000;
        const timestamp = new Date(new Date(from).toDateString()).valueOf(); // Date without time

        if (!this.subjects.has(timestamp)) {
          this.subjects.set(timestamp, []);
        }

        this.subjects.get(timestamp)!.push(subject);

        this.begin = Math.min(this.begin, from);
        this.end = Math.max(this.end, from);
      }
    }

    this.subjects$.next(this.subjects);

    console.timeEnd('load');
  }

  private async sync(): Promise<void> {
    console.log('Sync start')
    console.time('sync');

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
          const json = JSON.stringify(subjects);
          const compressed = LZ.compress(json);
          localStorage.setItem(`G-${group.id}`, compressed);

          // for (const subject of subjects) {
          //   const key = new Date(new Date(subject.from * 1000).toDateString()).valueOf(); // Date without time
          //
          //   if (!this.subjects.has(key)) {
          //     this.subjects.set(key, []);
          //   }
          //
          //   this.subjects.get(key)!.push(subject);
          // }
        }
      }
    }

    console.timeEnd('sync');
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
