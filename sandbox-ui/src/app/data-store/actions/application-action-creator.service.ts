import {Injectable} from '@angular/core';
import {Action, Store} from "@ngrx/store";
import {MAIN_MENU_TOGGLE, RELOAD_DATA} from "../reducers/app-reducer";
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

    realodData(){
        this.store.dispatch({
            type : RELOAD_DATA
        })
    }

}
