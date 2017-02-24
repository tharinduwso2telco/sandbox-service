import {IUserInfo} from "./interfaces/userInfo";
import {UserNumber} from "./manage-numbers-model";
import {Observable, BehaviorSubject} from "rxjs";


export interface IApplicationData {
    isMainMenuExpand: boolean,
    isReloadTriggered: boolean
}


export interface ITableFieldData {
    dataFieldName: string,
    tblColumnHead: string
}

export interface IManageNumberState {
    isEditorPanelOpen: boolean,
    selectedNumber: any,
    numbersTableData: {
        component: any,
        inputData: {
            tableDataSource: UserNumber[],
            fieldNames: ITableFieldData[]
        }
    }
}

export interface ApiInfo {
    description: string;
    path: string;
}

export interface ApiCategory {
    name: string;
    displayName?: string,
    iconClass?: string;
    swaggerDefinitions?: string[]
}

export interface ApiServiceDefinition {
    apiType: string;
    apiDefinitions: Api[];
}

export interface ServiceTypeCategory {
    apiType: string,
    config: any[],
    services: any[]
}

export interface Parameter {
    name: string;
    description?: string;
    required?: boolean;
    type?: string;
    paramType?: string;
    allowMultiple?: boolean;
    formControlType?: 'TEXTBOX'|'TEXT_AREA'|'RADIO'|'CHECKBOX'|'DROPDOWN';
    misc?:any;
}

export interface Operation {
    method: string;
    summary: string;
    notes: string;
    type: string;
    nickname: string;
    parameters: Parameter[]
}

export interface Api {
    name: string,
    displayName: string,
    path: string;
    operations: Operation[];
}

export interface DataProviderConfig {
    fieldName: string;
    formControlType: 'TEXTBOX'|'TEXT_AREA'|'RADIO'|'CHECKBOX'|'DROPDOWN';
    reducer: string;
    storeDataItem: string;
    dataItemLabelField: string;
    dataItemValueField: string;
    dataLoadServiceCall: string;
}



export interface ServiceConfig {
    name: string,
    apiType: string
    api: string;
    overrides : Parameter[];
}

export interface DynamicApiCallResult {
    api: Api;
    response: any;
    request: any;
    headers: any;
}

export interface IApiState {
    apiTypes: ApiCategory[],
    selectedApiType: ApiCategory,
    serviceTypes: ServiceTypeCategory,
    apiRequestModels: any,
    apiServiceDefinitions: {
        [name: string]: ApiServiceDefinition;
    },
    selectedApiConfig: ServiceConfig;
    resultDynamicApiCall: Map<string,DynamicApiCallResult>;
}

export interface IAppState {
    userInfo: IUserInfo,
    appSettings: IApplicationData,
    manageNumbers: IManageNumberState,
    apiState: IApiState
}

export interface DynamicComponentData {
    component: any,
    inputData: any,
    eventBinding: {
        eventName: string,
        subscriber: any,
        context: any
    }[]
}


export interface FormElementObservableMapper {
    observable: BehaviorSubject<any>;
    config: DataProviderConfig;
}