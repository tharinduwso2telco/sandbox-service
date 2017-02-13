import {IApplicationData} from "../models/common-models";
import {Action} from "@ngrx/store";

export const MAIN_MENU_TOGGLE = 'MAIN_MENU_TOGGLE';

export function AppReducer(appSettings: IApplicationData = null, action: Action): IApplicationData {

    switch (action.type) {

        case MAIN_MENU_TOGGLE : {
            let flag: boolean;
            if (!!appSettings) {
                flag = !!!appSettings.isMainMenuExpand
                return Object.assign({},appSettings, {isMainMenuExpand: flag})
            } else {
                return {isMainMenuExpand: true};
            }
        }

        default :
            return appSettings

    }
}
