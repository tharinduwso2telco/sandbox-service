import {Injectable} from '@angular/core';
import {Store} from "@ngrx/store";
import {IAppState} from "../models/common-models";
import {OPEN_ADD_NUMBER} from "../reducers/manage-numbers-reducer";

@Injectable()
export class ManageActionCreatorService {

    constructor(private store: Store<IAppState>) {
    }

    openAddNumber() {
        this.store.dispatch({
            type: OPEN_ADD_NUMBER
        });
    }

}
