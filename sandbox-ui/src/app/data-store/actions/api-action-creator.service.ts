import {Injectable} from '@angular/core';
import {IAppState} from "../models/common-models";
import {Store} from "@ngrx/store";
import {SET_API_TYPES, SET_SELECTED_API_TYPE, SET_SERVICE_TYPES} from "../reducers/api-reducer";

@Injectable()
export class ApiActionCreatorService {

    constructor(private store: Store<IAppState>) {
    }

    updateApiTypes(apiTypes: string[]) {
        this.store.dispatch({
            type: SET_API_TYPES,
            payload: apiTypes
        })
    }

    setSelectedApiType(apiType: string) {
        let type = apiType.toLowerCase();

        this.store.dispatch({
            type: SET_SELECTED_API_TYPE,
            payload: type
        });
    }

    setApiServiceTypes(serviceTypes: string[]) {
        this.store.dispatch({
            type: SET_SERVICE_TYPES,
            payload: serviceTypes
        });
    }
}
