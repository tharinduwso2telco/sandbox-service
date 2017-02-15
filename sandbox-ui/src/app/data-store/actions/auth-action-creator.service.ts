import {Injectable} from '@angular/core';
import {Store} from "@ngrx/store";
import {SET_LOGIN_DATA, CLEAR_LOGIN_DATA} from "../reducers/authentication-reducer";
import {IAppState} from "../models/common-models";
import {IUserInfo} from "../models/interfaces/userInfo";

@Injectable()
export class AuthActionCreatorService {

    constructor(private store: Store<IAppState>) {
    }

    setLoginData(loginInfo: IUserInfo) {
        this.store.dispatch({
            type: SET_LOGIN_DATA,
            payload: loginInfo
        });
    }

    clearLoginData() {
        this.store.dispatch({
            type: CLEAR_LOGIN_DATA,
            payload: null
        });
    }

}
