import {IUserInfo} from "./interfaces/userInfo";
export interface IApplicationData {
    isMainMenuExpand: boolean
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

export interface IManageNumberState {
    isEditorPanelOpen: boolean,
    selectedNumber: any,
    allNumbers: IUserNumber[]
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