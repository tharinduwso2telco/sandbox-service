import {Injectable} from '@angular/core';
import {Action, Store} from "@ngrx/store";
import {MAIN_MENU_TOGGLE} from "../reducers/app-reducer";
import {IAppState} from "../models/common-models";

@Injectable()
export class ApplicationActionCreatorService {

    constructor(private store: Store<IAppState>) {
    }

    toggleMainMenu() {
        this.store.dispatch({
            type: MAIN_MENU_TOGGLE
        });
    }

}
