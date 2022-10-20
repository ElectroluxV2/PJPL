export interface DataState {
  semesters: Record<string, string>;
  selectedSemestersIds: string[];
}

export const initialDataState: DataState = {
  semesters: {},
  selectedSemestersIds: []
}
