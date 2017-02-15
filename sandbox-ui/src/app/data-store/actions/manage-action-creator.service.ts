import {Injectable} from '@angular/core';
import {Store} from "@ngrx/store";
import {IAppState} from "../models/common-models";
import {OPEN_ADD_NUMBER, UPDATE_USER_NUMBERS, ADD_USER_NUMBER} from "../reducers/manage-numbers-reducer";
import {UserNumber} from "../models/manage-numbers-model";

@Injectable()
export class ManageActionCreatorService {

    constructor(private store: Store<IAppState>) {
    }

    openAddNumber() {
        this.store.dispatch({
            type: OPEN_ADD_NUMBER
        });
    }

    updateUserNumbers(numbers: UserNumber[] = []) {
        this.store.dispatch({
            type: UPDATE_USER_NUMBERS,
            payload: numbers
        });
    }

}
