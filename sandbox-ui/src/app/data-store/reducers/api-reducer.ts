import {IApiState, DynamicApiCallResult} from "../models/common-models";
import {Action} from "@ngrx/store";

const initialApiState: IApiState = {
    apiTypes: [],
    selectedApiType : null,
    serviceTypes : [],
    selectedApi : null,
    apiRequestModels : {},
    resultDynamicApiCall : new Map<string,DynamicApiCallResult>()
};

export const SET_API_TYPES: string = 'SET_API_TYPES';
export const SET_SELECTED_API_TYPE: string = 'SET_SELECTED_API_TYPE';
export const SET_SERVICE_TYPES: string = 'SET_SERVICE_TYPES';
export const UPDATE_API_REQ_MODELS: string = 'UPDATE_API_REQ_MODELS';
export const SET_SELECTED_API: string = 'SET_SELECTED_API';
export const UPDATE_DYNAMIC_API_RESULT: string = 'UPDATE_DYNAMIC_API_RESULT';

export function ApiReducer(apiState: IApiState = initialApiState, action: Action): IApiState {
    switch (action.type) {

        case SET_API_TYPES : {
            return Object.assign({}, apiState, {apiTypes: action.payload || []});
        }

        case SET_SELECTED_API_TYPE : {
            return Object.assign({},apiState,{selectedApiType:action.payload || null});
        }

        case SET_SERVICE_TYPES : {
            return Object.assign({},apiState,{serviceTypes:action.payload || []});
        }

        case UPDATE_API_REQ_MODELS : {
            return Object.assign({},apiState, Object.assign(apiState.apiRequestModels,action.payload));
        }

        case SET_SELECTED_API : {
            return Object.assign({},apiState,{selectedApi:action.payload});
        }

        case UPDATE_DYNAMIC_API_RESULT : {
            let tmp = {};
            tmp[(<DynamicApiCallResult>action.payload).api.name] = action.payload;
            return Object.assign({},apiState,{resultDynamicApiCall:Object.assign(apiState.resultDynamicApiCall,tmp)});
        }

        default : {
            return apiState;
        }
    }
}