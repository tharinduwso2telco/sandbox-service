import {IUserInfo} from "./interfaces/userInfo";
export interface IApplicationData {
    isMainMenuExpand: boolean,
    isReloadTriggered:boolean
}

export interface IUserNumber {
    number: string;
    mnc: number;
    balance: number;
    reserved_amount: number;
    imsi: number;
    description: string;
    status: number;
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
            tableDataSource:IUserNumber[],
            fieldNames:ITableFieldData[]
        }
    }
}

export interface IAppState {
    userInfo: IUserInfo,
    appSettings: IApplicationData,
    manageNumbers: IManageNumberState
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