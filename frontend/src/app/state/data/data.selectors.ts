import {createFeatureSelector, createSelector} from "@ngrx/store";
import {DataState} from "./data.state";


export const getDataState = createFeatureSelector<DataState>('data');

export const getSemesters = createSelector(
  getDataState,
  state => state.semesters
);

export const getSemestersList = createSelector(
  getSemesters,
  semesters => Object.entries(semesters)
);
