import {IUserInfo} from "./authentocation-models";

export interface IApplicationData{
    isMainMenuExpand : boolean
}

export interface IAppState {
    userInfo: IUserInfo,
    appSettings : IApplicationData
}