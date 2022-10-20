import {Actions, createEffect, ofType} from "@ngrx/effects";
import {DataService} from "../../services/data.service";
import {
  addSemesterToSelected,
  loadSemesters,
  removeSemesterFromSelected,
  setSemesters,
  setStudies
} from "./data.actions";
import {distinctUntilChanged, forkJoin, map, mergeMap, Observable, switchMap, withLatestFrom} from "rxjs";
import {Injectable} from "@angular/core";
import {getSemestersList} from "./data.selectors";
import {Store} from "@ngrx/store";
import {DataState} from "./data.state";

@Injectable()
export class DataEffects {
  constructor(
    private readonly actions$: Actions,
    private readonly dataService: DataService,
    private readonly store: Store<DataState>
  ) { }

  loadSemesters$ = createEffect(() => this.actions$.pipe(
    ofType(loadSemesters),
    switchMap(() => this.dataService.loadSemesters()),
    map(semesters => setSemesters({ semesters }))
  ));

  // @ts-ignore
  loadStudies$ = createEffect(() => this.actions$.pipe(
    ofType(setSemesters),
    withLatestFrom(this.store.select(getSemestersList)),
    mergeMap(([action, semesters]) => forkJoin(Object.fromEntries(semesters.map(semester => [semester.id, this.dataService.loadStudies(semester.id)])))),
    map(studies => {
      console.log(studies);
      return setStudies()
    })
  ))
}
