import { Injectable } from '@angular/core';
import {map, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

export interface Metadata {
  updated: number;
  study: string;
  name: string;
  semester: string;
}

export interface Event {
  from: number;
  to: number;
  room: string;
  location: string;
  groups: string[];
  color: string;
  additionalData: Record<string, string>;
}

export type Subject = Event & Metadata;

export interface IndexItem {
  name: string;
  id: string;
}

export type Semester = IndexItem;
export type Study = IndexItem;
export type Group = IndexItem;

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private static readonly API = "//s2.budziszm.pl/data";

  constructor(private readonly http: HttpClient) { }

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

  public loadSemesters(): Observable<Record<string, string>> {
    return this.getIndex('semesters');
  }

  public loadStudies(semesterId: string): Observable<Record<string, string>> {
    return this.getIndex(`${semesterId}/studies`);
  }

  public loadGroups(studyId: string): Observable<Record<string, string>> {
    return this.getIndex(`${studyId}/groups`);
  }

  private getIndex(path: string): Observable<Record<string, string>> {
    return this.get(`${path}.json`);
  }

  private get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`${ApiService.API}/${endpoint}`);
  }
}
