import {DataState, initialDataState} from "./data/data.state";

export interface State {
  data: DataState
}

export const initialState: State = {
  data: initialDataState
}
