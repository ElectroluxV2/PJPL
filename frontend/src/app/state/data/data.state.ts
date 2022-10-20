export interface DataState {
  semesters: Record<string, string>;
  selectedSemestersIds: string[];
  studies: Record<string, Record<string, string>>;
}

export const initialDataState: DataState = {
  semesters: {},
  selectedSemestersIds: [],
  studies: {}
}
