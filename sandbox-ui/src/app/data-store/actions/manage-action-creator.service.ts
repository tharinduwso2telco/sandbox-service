import {Injectable} from '@angular/core';
import {Store} from "@ngrx/store";
import {IAppState, IUserNumber} from "../models/common-models";
import {OPEN_ADD_NUMBER, UPDATE_USER_NUMBERS} from "../reducers/manage-numbers-reducer";

@Injectable()
export class ManageActionCreatorService {

    constructor(private store: Store<IAppState>) {
    }

    openAddNumber() {
        this.store.dispatch({
            type: OPEN_ADD_NUMBER
        });
    }

    updateUserNumbers(numbers: IUserNumber[] = []) {
        this.store.dispatch({
            type: UPDATE_USER_NUMBERS,
            payload: numbers
        });
    }

}
