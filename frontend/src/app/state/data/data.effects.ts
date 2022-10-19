import {Actions, createEffect, ofType} from "@ngrx/effects";
import {DataService} from "../../services/data.service";
import {loadSemesters, setSemesters} from "./data.actions";
import {map, mergeMap} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable()
export class DataEffects {
  constructor(private actions$: Actions, private dataService: DataService) { }

  loadSemesters$ = createEffect(() => this.actions$.pipe(
    ofType(loadSemesters),
    mergeMap(() => this.dataService.loadSemesters().pipe(
      map(semesters => setSemesters({ semesters }))
    ))
  ));
}
