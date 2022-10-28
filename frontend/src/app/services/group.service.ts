import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {Group} from "./data.service";

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private readonly selectedGroups = new Map<string, string>();
  public readonly selectedGroups$ = new BehaviorSubject<Group[]>([]);

  constructor() { }

  selectGroup(g1: string) {
    this.selectedGroups.set(`g-${Math.random()}`, `${Math.random()}`);
    this.selectedGroups$.next(Array.from(this.selectedGroups.entries()).map(([id, name]) => ({id, name})));
  }

  removeGroup(id: string) {
    this.selectedGroups.delete(id);
    this.selectedGroups$.next(Array.from(this.selectedGroups.entries()).map(([id, name]) => ({id, name})));
  }
}
