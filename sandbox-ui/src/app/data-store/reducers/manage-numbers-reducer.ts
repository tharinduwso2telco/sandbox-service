import {IManageNumberState} from "../models/common-models";
import {Action} from "@ngrx/store";
import {DynamicDataTableDefaultTableComponent} from "../../shared/components/dynamic-data-table-default-table/dynamic-data-table-default-table.component";
import {UserNumber} from "../models/manage-numbers-model";


export const ADD_NUMBER: string = 'ADD_NUMBER';
export const EDIT_NUMBER: string = 'EDIT_NUMBER';
export const OPEN_EDITOR_PANEL: string = 'OPEN_EDITOR_PANEL';
export const CLOSE_EDITOR_PANEL: string = 'CLOSE_EDITOR_PANEL';
export const UPDATE_USER_NUMBERS: string = 'UPDATE_USER_NUMBERS';


const initialState: IManageNumberState = {
    isEditorPanelOpen: false,
    selectedNumber: new UserNumber(),
    numbersTableData: {
        component: DynamicDataTableDefaultTableComponent,
        inputData: {
            tableDataSource: null,
            fieldNames: [
                {
                    dataFieldName: 'number',
                    tblColumnHead: 'Number'

                },
                {
                    dataFieldName: 'description',
                    tblColumnHead: 'Description'

                },
                {
                    dataFieldName: 'numberBalance',
                    tblColumnHead: 'Balance'

                },
                {
                    dataFieldName: 'reservedAmount',
                    tblColumnHead: 'Reserved Amount'

                },
                {
                    dataFieldName: 'imsi',
                    tblColumnHead: 'IMSI'

                }
            ]
        }
    }
};

export function ManageNumberReducer(manageNumberState: IManageNumberState = initialState, action: Action): IManageNumberState {
    switch (action.type) {

        case ADD_NUMBER : {
            return Object.assign({}, manageNumberState,
                {
                    selectedNumber: new UserNumber()
                }
            );
        }

        case EDIT_NUMBER : {
            return Object.assign({}, manageNumberState,
                {
                    selectedNumber: action.payload
                }
            );
        }

        case OPEN_EDITOR_PANEL : {
            return Object.assign({}, manageNumberState,
                {
                    isEditorPanelOpen: true
                }
            );
        }

        case CLOSE_EDITOR_PANEL : {
            return Object.assign({}, manageNumberState,
                {
                    isEditorPanelOpen: false
                }
            );
        }

        case UPDATE_USER_NUMBERS : {
            return Object.assign({}, manageNumberState, {
                numbersTableData: Object.assign({}, manageNumberState.numbersTableData, {
                    inputData: Object.assign({}, manageNumberState.numbersTableData.inputData, {
                        tableDataSource: action.payload || []
                    })
                })
            })
        }


        default : {
            return manageNumberState;
        }
    }
}