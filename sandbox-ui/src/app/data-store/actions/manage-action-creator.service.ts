import {Injectable} from '@angular/core';
import {Store} from "@ngrx/store";
import {IAppState} from "../models/common-models";
import {
    UPDATE_USER_NUMBERS, ADD_NUMBER, EDIT_NUMBER, OPEN_EDITOR_PANEL,
    CLOSE_EDITOR_PANEL
} from "../reducers/manage-numbers-reducer";
import {UserNumber} from "../models/manage-numbers-model";

@Injectable()
export class ManageActionCreatorService {

    constructor(private store: Store<IAppState>) {
    }

    addNumber() {
        this.store.dispatch({type: ADD_NUMBER});
        this.store.dispatch({type: OPEN_EDITOR_PANEL});
    }

    editNumber(number: UserNumber) {
        this.store.dispatch({type: EDIT_NUMBER, payload: number});
        this.store.dispatch({type: OPEN_EDITOR_PANEL});
    }

    closeEditorPanel(){
        this.store.dispatch({type: CLOSE_EDITOR_PANEL});
    }

    updateUserNumbers(numbers: UserNumber[] = []) {
        this.store.dispatch({
            type: UPDATE_USER_NUMBERS,
            payload: numbers
        });
    }
}
