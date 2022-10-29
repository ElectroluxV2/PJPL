import { Injectable } from '@angular/core';
import {BehaviorSubject, firstValueFrom, Subject} from "rxjs";
import {DataService, Group, Semester, Study} from "./data.service";

export type GroupWithMetadata = Group & {
  semesterName: string; // semester id is part of group id
  studyName: string; // study id is part of group id
};

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private readonly selectedGroups = new Map<string, string>();
  public readonly selectedGroups$ = new BehaviorSubject<Group[]>([]);
  public readonly availableSemesters$ = new BehaviorSubject<Semester[]>([]);
  public readonly availableStudies$ = new BehaviorSubject<Study[]>([]);
  private readonly availableGroups = new Map<string, GroupWithMetadata[]>();

  constructor(private readonly dataService: DataService) {
    void this.loadAvailableGroups();
  }

  private async loadAvailableGroups(): Promise<void> {
    const semesters: Semester[] = Object
      .entries(await firstValueFrom(this.dataService.loadSemesters()))
      .map(([name, id]) => ({name, id}));

    this.availableSemesters$.next(semesters);

    const allStudies: Study[] = [];

    for (const semester of semesters) {
      const studies: Study[] = Object
          .entries(await firstValueFrom(this.dataService.loadStudies(semester.id)))
          .map(([name, id]) => ({name, id: `${semester.id}/${id}`}));

      allStudies.push(...studies);

      for (const study of studies) {
        const groups: GroupWithMetadata[] = Object
              .entries(await firstValueFrom(this.dataService.loadGroups(study.id)))
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
    console.log(id)
    this.selectedGroups$.next(Array.from(this.selectedGroups.entries()).map(([id, name]) => ({id, name})));
  }

  public removeGroup(id: string): void {
    console.log(id)
    this.selectedGroups.delete(id);
    this.selectedGroups$.next(Array.from(this.selectedGroups.entries()).map(([id, name]) => ({id, name})));
  }

  public isSelected(groupId: string): boolean {
    return this.selectedGroups.has(groupId);
  }

  public getGroups(studyId: string): GroupWithMetadata[] {
    return this.availableGroups.get(studyId)!.sort((a, b) => a.name.localeCompare(b.name));
  }

  public toggle(group: Group): void {
    if (this.isSelected(group.id)) {
      this.removeGroup(group.id);
    } else {
      this.selectGroup(group.id, group.name);
    }
  }
}
