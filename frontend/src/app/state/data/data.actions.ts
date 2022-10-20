import {createAction, props} from '@ngrx/store';

export const loadSemesters = createAction(
  'Load semesters'
);

export const setSemesters = createAction(
  'Set semesters',
  props<{ semesters: Record<string, string> }>()
);

export const addSemesterToSelected = createAction(
  'Add semester to selected',
  props<{ semesterId: string }>()
);

export const removeSemesterFromSelected = createAction(
  'Remove semester from selected',
  props<{ semesterId: string }>()
);

export const setStudies = createAction(
  'Set studies',
);
