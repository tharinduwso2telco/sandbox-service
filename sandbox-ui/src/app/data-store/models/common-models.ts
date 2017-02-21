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

export interface ApiInfo{
    description : string;
    path : string;
}

export interface ApiCategory{
    name:string;
    displayName?:string,
    iconClass?:string;
    apis : ApiInfo[];
}

export interface ServiceTypeCategory{
    name : string,
    endPoints : any[]
}

export interface IApiState{
    apiTypes : ApiCategory[],
    selectedApiType : ApiCategory,
    serviceTypes : ServiceTypeCategory[],
    apiRequestModels : any
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