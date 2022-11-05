import { Injectable } from '@angular/core';
import {BehaviorSubject, firstValueFrom} from "rxjs";
import {ApiService, Group, Semester, Study} from "./api.service";

export type GroupWithMetadata = Group & {
  semesterName: string; // semester id is part of group id
  studyName: string; // study id is part of group id
};

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private static readonly selectedGroupsKey = 'selected-groups';
  private readonly selectedGroups = new Map<string, string>();
  public readonly selectedGroups$ = new BehaviorSubject<Group[]>([]);
  public readonly availableSemesters$ = new BehaviorSubject<Semester[]>([]);
  public readonly availableStudies$ = new BehaviorSubject<Study[]>([]);
  private readonly availableGroups = new Map<string, GroupWithMetadata[]>();

  constructor(private readonly apiService: ApiService) {
    void this.loadAvailableGroups();
    void this.loadSavedSelectedGroups();
  }

  private async loadAvailableGroups(): Promise<void> {
    const semesters: Semester[] = Object
      .entries(await firstValueFrom(this.apiService.loadSemesters()))
      .map(([name, id]) => ({name, id}));

    this.availableSemesters$.next(semesters);

    const allStudies: Study[] = [];

    for (const semester of semesters) {
      const studies: Study[] = Object
          .entries(await firstValueFrom(this.apiService.loadStudies(semester.id)))
          .map(([name, id]) => ({name, id: `${semester.id}/${id}`}));

      allStudies.push(...studies);

      for (const study of studies) {
        const groups: GroupWithMetadata[] = Object
              .entries(await firstValueFrom(this.apiService.loadGroups(study.id)))
              .map(([name, id]) => ({
                name,
                id: `${study.id}/${id}`,
                semesterName: semester.name,
                studyName: study.name
              }));

        this.availableGroups.set(study.id, groups);
      }
    }

    this.availableStudies$.next(allStudies);
  }

  public selectGroup(id: string, name: string): void {
    this.selectedGroups.set(id, name);
    const newValue = Array.from(this.selectedGroups.entries()).map(([id, name]) => ({id, name}));
    this.selectedGroups$.next(newValue);
    localStorage.setItem(GroupService.selectedGroupsKey, JSON.stringify(newValue));
  }

  public deselectGroup(id: string): void {
    this.selectedGroups.delete(id);
    const newValue = Array.from(this.selectedGroups.entries()).map(([id, name]) => ({id, name}));
    this.selectedGroups$.next(newValue);
    localStorage.setItem(GroupService.selectedGroupsKey, JSON.stringify(newValue));
  }

  public isGroupSelected(groupId: string): boolean {
    return this.selectedGroups.has(groupId);
  }

  public getGroups(studyId: string): GroupWithMetadata[] {
    return this.availableGroups
      .get(studyId)!
      .sort((a, b) => a.name.localeCompare(b.name));
  }

  public toggleGroupSelection(group: Group): void {
    if (this.isGroupSelected(group.id)) {
      this.deselectGroup(group.id);
    } else {
      this.selectGroup(group.id, group.name);
    }
  }

  private loadSavedSelectedGroups() {
    const saved: Group[] = JSON.parse(localStorage.getItem(GroupService.selectedGroupsKey) ?? '[]');
    this.selectedGroups$.next(saved);
    for (const {name, id} of saved) {
      this.selectedGroups.set(id, name);
    }
  }
}
