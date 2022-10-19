import {createAction, props} from '@ngrx/store';

export const loadSemesters = createAction(
  'Load semesters'
);

export const setSemesters = createAction(
  'Set semesters',
  props<{ semesters: Record<string, string> }>()
);
