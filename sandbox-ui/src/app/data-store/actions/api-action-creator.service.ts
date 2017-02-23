import {Injectable} from '@angular/core';
import {
    IAppState, ApiCategory, ServiceTypeCategory, Api, DynamicApiCallResult,
    ApiServiceDefinition, ServiceConfig
} from "../models/common-models";
import {Store} from "@ngrx/store";
import {
    SET_API_TYPES, SET_SELECTED_API_TYPE, SET_SERVICE_TYPES, UPDATE_API_REQ_MODELS,
    UPDATE_DYNAMIC_API_RESULT, UPDATE_API_SERVICE_DEFINITIONS, SET_SELECTED_SERVICE_CONFIG
} from "../reducers/api-reducer";


@Injectable()
export class ApiActionCreatorService {

    constructor(private store: Store<IAppState>,) {
    }

    updateApiTypes(apiTypes: ApiCategory[]) {
        this.store.dispatch({
            type: SET_API_TYPES,
            payload: apiTypes
        })
    }

    setSelectedApiType(apiCat: ApiCategory) {
        this.store.dispatch({
            type: SET_SELECTED_API_TYPE,
            payload: apiCat
        });
    }

    setApiServiceTypes(serviceTypes: any) {
        this.store.dispatch({
            type: SET_SERVICE_TYPES,
            payload: serviceTypes
        });
    }

    updateApiRequestModels(models: any) {
        this.store.dispatch({
            type: UPDATE_API_REQ_MODELS,
            payload: models
        });
    }

    setSelectedServiceConfig(config: ServiceConfig) {
        this.store.dispatch({
            type: SET_SELECTED_SERVICE_CONFIG,
            payload: config
        });
    }

    setApiServiceDefinitions(definition: ApiServiceDefinition) {
        this.store.dispatch({
            type: UPDATE_API_SERVICE_DEFINITIONS,
            payload: definition
        })
    }

    setDynamicApiCallResult(apiResult: DynamicApiCallResult) {
        this.store.dispatch({
            type: UPDATE_DYNAMIC_API_RESULT,
            payload: apiResult
        });
    }


}
