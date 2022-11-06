import {Injectable} from '@angular/core';
import {BehaviorSubject, firstValueFrom} from "rxjs";
import * as LZ from 'lz-string';
import {ApiService, Subject} from "./api.service";
import {GroupService} from "./group.service";

export interface GroupSyncInfo {
  groupId: string;
  groupName: string;
  updated: number;
  synchronized: number;
}


@Injectable({
  providedIn: 'root'
})
export class DataService {
  public readonly subjectsByGroupId = new Map<string, Subject[]>();
  public readonly subjectsByTimestamp = new Map<number, Subject[]>();
  public readonly subjectsChanged$ = new BehaviorSubject<void>(undefined);

  public firstValuableDay: number = Number.MAX_VALUE;
  public lastValuableDay: number = 0;

  constructor(
    private readonly groupsService: GroupService,
    private readonly apiService: ApiService
  ) {
    this.groupsService.selectedGroups$
      // if group will not be found in storage it will be downloaded, otherwise it will just load faster but will refresh anyway, we do this in parallel
      .subscribe(savedGroups => savedGroups.forEach(group => void this.loadGroupFromStorage(group.id)));
  }

  private async synchronizeGroup(groupId: string): Promise<void> {
    const subjects: Subject[] = await firstValueFrom(this.apiService.loadSubjects(groupId));
    this.updateSubjectsState(groupId, subjects);
    // console.log(`Loaded ${subjects.length} subjects for group ${groupId} from api.`);
    const json = JSON.stringify(subjects);
    const compressed = LZ.compress(json);
    localStorage.setItem(`G-${groupId}`, compressed);
  }

  private async loadGroupFromStorage(groupId: string): Promise<void> {
    if (!(`G-${groupId}` in localStorage)) {
      console.warn(`Group ${groupId} not found in storage, synchronizing now.`);
      return this.synchronizeGroup(groupId);
    }

    const compressed = localStorage.getItem(`G-${groupId}`)!;
    const json = LZ.decompress(compressed)!;
    const subjects: Subject[] = JSON.parse(json) ?? [];
    // console.log(`Loaded ${subjects.length} subjects for group ${groupId} from localStorage.`);
    this.updateSubjectsState(groupId, subjects);

    // Load fresh data anyway, used by automated synchronizer
    return this.synchronizeGroup(groupId);
  }

  private updateSubjectsState(groupId: string, subjects: Subject[]): void {
    this.subjectsByGroupId.set(groupId, subjects);

    this.subjectsByTimestamp.clear();
    for (const subjects of this.subjectsByGroupId.values()) {
      for (const subject of subjects) {
        const timestamp = new Date(new Date(subject.from).toDateString()).valueOf(); // Date without time

        if (!this.subjectsByTimestamp.has(timestamp)) {
          this.subjectsByTimestamp.set(timestamp, []);
        }

        this.subjectsByTimestamp.get(timestamp)!.push(subject);

        this.firstValuableDay = Math.min(this.firstValuableDay, timestamp);
        this.lastValuableDay = Math.max(this.lastValuableDay, timestamp);
      }
    }

    this.subjectsChanged$.next();
  }
}
