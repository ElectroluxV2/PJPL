import {createFeatureSelector, createSelector} from "@ngrx/store";
import {DataState} from "./data.state";

export const getDataState = createFeatureSelector<DataState>('data');

export const getSemesters = createSelector(
  getDataState,
  state => state.semesters
);

export const getSemestersList = createSelector(
  getSemesters,
  semesters => Object.entries(semesters).map(([name, id]) => ({name, id}))
);

export const getSelectedSemestersIds = createSelector(
  getDataState,
  state => state.selectedSemestersIds
);

export const getStudies = createSelector(
  getDataState,
  store => store.studies
);

export const getStudiesList = createSelector(
  getStudies,
  studies => Object.entries(studies).map(([semesterId, studiesIndex]) => ({
    name: semesterId,
    studies: Object.entries(studiesIndex).map(([name, id]) => ({name, id}))
  }))
);
