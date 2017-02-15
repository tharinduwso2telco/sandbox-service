import {IManageNumberState} from "../models/common-models";
import {Action} from "@ngrx/store";

export const OPEN_ADD_NUMBER: string = 'OPEN_ADD_NUMBER';
export const UPDATE_USER_NUMBERS: string = 'UPDATE_USER_NUMBERS';

const initialState: IManageNumberState = {
    isEditorPanelOpen: false,
    selectedNumber: null,
    allNumbers:[]

};

export function ManageNumberReducer(manageNumberState: IManageNumberState = initialState, action: Action): IManageNumberState {
    switch (action.type) {

        case OPEN_ADD_NUMBER : {
            return Object.assign({}, manageNumberState,
                {
                    isEditorPanelOpen: !manageNumberState.isEditorPanelOpen,
                    selectedNumber : null
                }
            );
        }

        case UPDATE_USER_NUMBERS : {
            return Object.assign({},manageNumberState,
                {
                    allNumbers : action.payload || []
                }
            )
        }


        default : {
            return manageNumberState;
        }
    }
}