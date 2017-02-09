import {Injectable} from '@angular/core';
import {Action} from "@ngrx/store";
import {SET_LOGIN_DATA,CLEAR_LOGIN_DATA} from "../reducers/authentication-reducer";
import {IUserInfo} from "../models/authentocation-models";

@Injectable()
export class AuthActionCreatorService {

    constructor() {
    }

    setLoginData(loginInfo: IUserInfo): Action {
        return {
            type: SET_LOGIN_DATA,
            payload: loginInfo
        }
    }

    clearLoginData():Action{
        return{
            type : CLEAR_LOGIN_DATA,
            payload : null
        }
    }

}
