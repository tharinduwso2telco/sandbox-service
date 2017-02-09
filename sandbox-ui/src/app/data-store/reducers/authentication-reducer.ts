import {IUserInfo} from "../models/authentocation-models";
import {Action} from '@ngrx/store'

export const SET_LOGIN_DATA = 'SET_LOGIN_DATA';
export const CLEAR_LOGIN_DATA = 'CLEAR_LOGIN_DATA';

export function AuthenticationReducer(userInfo: IUserInfo = null, action: Action): IUserInfo {
    switch (action.type) {

        case SET_LOGIN_DATA : {
            return action.payload;
        }

        case CLEAR_LOGIN_DATA : {
            return null;
        }

        default :
            return userInfo;
    }
}