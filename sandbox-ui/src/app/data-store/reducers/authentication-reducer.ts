import {Action} from '@ngrx/store'
import {IUserInfo} from "../models/interfaces/userInfo";

export const SET_LOGIN_DATA = 'SET_LOGIN_DATA';
export const CLEAR_LOGIN_DATA = 'CLEAR_LOGIN_DATA';

export function AuthenticationReducer(userInfo: IUserInfo = null, action: Action): IUserInfo {
    switch (action.type) {

        case SET_LOGIN_DATA : {
            sessionStorage.setItem('userInfo', JSON.stringify(action.payload));
            return action.payload;
        }

        case CLEAR_LOGIN_DATA : {
            sessionStorage.setItem('userInfo', null);
            return null;
        }

        default :{
            let tmp = JSON.parse(sessionStorage.getItem('userInfo'));
            return userInfo || tmp;
        }

    }
}