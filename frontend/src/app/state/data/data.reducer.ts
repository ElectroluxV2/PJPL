import {createReducer, on} from '@ngrx/store';
import {DataState, initialDataState} from "./data.state";
import {setSemesters} from "./data.actions";

export const dataReducer = createReducer<DataState>(
  initialDataState,
  on(setSemesters, (state, { semesters }): DataState => ({
    ...state,
    semesters
  }))
);
