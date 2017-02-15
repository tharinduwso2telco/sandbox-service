import {IManageNumberState} from "../models/common-models";
import {Action} from "@ngrx/store";
import {DynamicDataTableDefaultTableComponent} from "../../shared/components/dynamic-data-table-default-table/dynamic-data-table-default-table.component";

export const OPEN_ADD_NUMBER: string = 'OPEN_ADD_NUMBER';
export const UPDATE_USER_NUMBERS: string = 'UPDATE_USER_NUMBERS';
export const ADD_USER_NUMBER: string = 'ADD_USER_NUMBER';

const initialState: IManageNumberState = {
    isEditorPanelOpen: false,
    selectedNumber: null,
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
                    dataFieldName: 'balance',
                    tblColumnHead: 'Balance'

                },
                {
                    dataFieldName: 'reserved_amount',
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

        case OPEN_ADD_NUMBER : {
            return Object.assign({}, manageNumberState,
                {
                    isEditorPanelOpen: !manageNumberState.isEditorPanelOpen,
                    selectedNumber: null
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