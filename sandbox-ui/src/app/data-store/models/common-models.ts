import {IUserInfo} from "./authentocation-models";

export interface IApplicationData{
    isMainMenuExpand : boolean
}

export interface IManageNumberState{
    isEditorPanelOpen :boolean,
    selectedNumber:any
}

export interface IAppState {
    userInfo: IUserInfo,
    appSettings : IApplicationData,
    manageNumbers : IManageNumberState
}

export interface DynamicComponentData{
    component:any,
    inputData:any,
    eventBinding : {
        eventName:string,
        subscriber : any,
        context:any
    }[]
}