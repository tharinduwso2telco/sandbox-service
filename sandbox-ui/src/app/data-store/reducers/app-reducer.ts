import {IApplicationData} from "../models/common-models";
import {Action} from "@ngrx/store";

export const MAIN_MENU_TOGGLE = 'MAIN_MENU_TOGGLE';
export const RELOAD_DATA = 'RELOAD_DATA';

const initialApplicationData: IApplicationData = {
    isMainMenuExpand: false,
    isReloadTriggered: false
};

export function AppReducer(appSettings: IApplicationData = initialApplicationData, action: Action): IApplicationData {

    switch (action.type) {

        case MAIN_MENU_TOGGLE : {
            return Object.assign({}, appSettings,
                {isMainMenuExpand: !appSettings.isMainMenuExpand}
            )
        }

        case RELOAD_DATA: {
            return Object.assign({}, appSettings,
                {isReloadTriggered: true}
            )
        }

        default :
            return appSettings

    }
}
