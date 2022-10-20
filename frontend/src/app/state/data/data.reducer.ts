import {createReducer, on} from '@ngrx/store';
import {DataState, initialDataState} from "./data.state";
import {addSemesterToSelected, removeSemesterFromSelected, setSemesters, setStudies} from "./data.actions";

export const dataReducer = createReducer<DataState>(
  initialDataState,
  on(setSemesters, (state, { semesters }): DataState => ({
    ...state,
    semesters
  })),
  on(addSemesterToSelected, (state, { semesterId }): DataState => ({
    ...state,
    selectedSemestersIds: Array.from(new Set(state.selectedSemestersIds.concat(semesterId)))
  })),
  on(removeSemesterFromSelected, (state, { semesterId }): DataState => {
    const asSet = new Set(state.selectedSemestersIds);
    asSet.delete(semesterId);
    return {
      ...state,
      selectedSemestersIds: Array.from(asSet)
    }
  }),
  on(setStudies, (state, { studies }): DataState => ({
    ...state,
    studies
  }))
);
