import {IUserInfo} from "./interfaces/userInfo";
import {UserNumber} from "./manage-numbers-model";


export interface IApplicationData {
    isMainMenuExpand: boolean,
    isReloadTriggered:boolean
}


export interface ITableFieldData{
    dataFieldName:string,
    tblColumnHead:string
}

export interface IManageNumberState {
    isEditorPanelOpen: boolean,
    selectedNumber: any,
    numbersTableData: {
        component : any,
        inputData:{
            tableDataSource:UserNumber[],
            fieldNames:ITableFieldData[]
        }
    }
}

export interface IApiState{
    apiTypes : string[],
    selectedApiType : string,
    serviceTypes : string[]
}

export interface IAppState {
    userInfo: IUserInfo,
    appSettings: IApplicationData,
    manageNumbers: IManageNumberState,
    apiState:IApiState
}

export interface DynamicComponentData {
    component: any,
    inputData: any,
    eventBinding: {
        eventName: string,
        subscriber: any,
        context: any
    }[]
}