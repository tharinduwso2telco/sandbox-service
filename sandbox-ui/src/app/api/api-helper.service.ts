import {Injectable, Inject} from '@angular/core';
import {
    IAppState, IApiState, ApiCategory, Api, Operation, Parameter,
    DynamicApiCallResult, ServiceConfig
} from "../data-store/models/common-models";
import {Store} from "@ngrx/store";
import {ApiRemoteService} from "../data-store/services/api-remote-service";
import {FormItemBase, TextInputFormItem, DropDownControl} from "../data-store/models/form-models";
import {Validators} from "@angular/forms";
import {Headers, RequestOptions, Http, Response} from "@angular/http";
import {ApiActionCreatorService} from "../data-store/actions/api-action-creator.service";
import {Observable, BehaviorSubject} from "rxjs";

@Injectable()
export class ApiHelperService {

    private apiState: IApiState;

    constructor(private apiRemoteService: ApiRemoteService,
                private store: Store<IAppState>,
                private http: Http,
                private actionCreator: ApiActionCreatorService,
                @Inject('SANDBOX_SERVER_PROXY_PATTERN') private sandboxPattern: string) {

        this.store.select('ApiData')
            .subscribe((state: IApiState) => {
                this.apiState = state;
            })
    }

    getFormModelForApi(api: Api) {
        let formModel: FormItemBase<any>[] = [];
        let operation: Operation = api.operations[0]; // only extract first operation. extend if need be
        if (!!operation) {
            operation.parameters.forEach((param: Parameter, index: number) => {

                switch (param.type) {
                    case 'string': {
                        if (!param.allowMultiple) {
                            formModel.push(this.getTextInput(param, index));
                        }
                        break;
                    }

                    default : {
                        let properties = this.getPropertiesFromComplexObject(param.type);
                        if (properties.length > 0) {
                            properties.forEach((prp, i) => {
                                formModel.push(this.getTextInput(prp, index + i));
                            })
                        }
                    }
                }
            });
        }

        //TEST
        let tmp:Parameter = {
            name: 'moduleClass',
            description: 'moduleClass',
            required: true,
            type: 'string',
            paramType: 's',
            allowMultiple: false,
            allowSelection :true
        };

        formModel.push(this.getDropdown(tmp,5));


        return formModel;
    }

    dynamicFormSubmit(formVal: any, api: Api) {
        if (formVal && api) {
            let operation: Operation = api.operations && api.operations[0];
            let path = api.path;

            let endPoint: string = this.buildPath(formVal, path, operation);
            let options: RequestOptions = new RequestOptions({headers: this.getHeaders(formVal, operation)});

            if (operation.method == 'GET') {
                this.http.get(this.sandboxPattern + endPoint, options)
                    .map((response: Response) => response.json())
                    .subscribe((result) => {
                        this.actionCreator.setDynamicApiCallResult(this.dynamicApiCallAdaptor(api, result, {}));
                    });
            } else if (operation.method == 'POST') {
                let params = {};
                this.getParametersFromOperation('body', operation)
                    .forEach((paramName: string) => {
                        params[paramName] = formVal[paramName] || '';
                    });
                this.http.post(this.sandboxPattern + endPoint, params, options)
                    .map((response: Response) => {
                        try{
                            return response.json();
                        }catch(e){
                            return response.text();
                        }
                    })
                    .subscribe((result) => {
                        this.actionCreator.setDynamicApiCallResult(this.dynamicApiCallAdaptor(api, result, params));
                    });

            }
        }
    }

    getApiFromConfig(config:ServiceConfig){
        if(!!this.apiState.apiServiceDefinitions[config.apiType]){
            let filtered =  this.apiState.apiServiceDefinitions[config.apiType].apiDefinitions
                .filter((api:Api)=>api.name == config.api);
            return (filtered.length > 0)? filtered[0] : null;
        }
        return null;
    }

    private dynamicApiCallAdaptor(api: Api, result: any, request: any): DynamicApiCallResult {
        let adapted: DynamicApiCallResult = {
            api: api,
            headers: '',
            response: result,
            request: request
        };
        return adapted;
    }

    private getHeaders(formVal: any, operation: Operation) {
        let headers: any = {
            'Content-Type': 'application/json'
        };

        this.getParametersFromOperation('header', operation)
            .forEach((paramName) => {
                headers[paramName] = formVal[paramName] || '';
            });

        return new Headers(headers);
    }

    private getParametersFromOperation(type: string, operation: Operation): string[] {
        let params: string[] = [];
        if (!!operation) {
            operation.parameters
                .filter((param: Parameter) => param.paramType == type)
                .forEach((param: Parameter) => {
                    if (param.type == 'string') {
                        params.push(param.name);
                    } else {
                        this.getPropertiesFromComplexObject(param.type)
                            .forEach((obj: any) => {
                                params.push(obj.name);
                            })
                    }
                });

        }
        return params;
    }


    private buildPath(formVal: any, path: string, operation: Operation): string {
        if (!!path && operation) {
            if (!!operation) {
                this.getParametersFromOperation('path', operation)
                    .forEach((paramName: string) => {
                        if (!!formVal[paramName]) {
                            path = path.replace('{' + paramName + '}', formVal[paramName]);
                        }
                    });
            }
        }
        return path;
    }


    private getTextInput(param: Parameter, index: number) {
        let tmp: FormItemBase<any> = new TextInputFormItem({
            key: param.name,
            label: (param.description && param.description != param.name) ? param.description : this.apiRemoteService.getNameFromCamelCase(param.name),
            order: index,
            value: '',
            required: param.required || false,
            validators: []
        });

        if (!!param.required) {
            tmp.validators.push(Validators.required);
        }
        return tmp;
    }

    private getDropdown(param:Parameter,index:number){
        let x = new BehaviorSubject<any[]>([]);

        let tmp:FormItemBase<any> = new DropDownControl({
            key: param.name,
            label: (param.description && param.description != param.name) ? param.description : this.apiRemoteService.getNameFromCamelCase(param.name),
            required: param.required || false,
            order: index,
            validators: [],
            dropDownOptions : x
        });
        x.next([
            {key:'one', value:'one'},
            {key:'two', value:'two'},
            {key:'three', value:'three'},
            {key:'four', value:'four'}
        ]);
        return tmp;
    }


    private getPropertiesFromComplexObject(name: string) {
        let flatProperties = [];
        let obj = this.getObjectFromModel(name);
        if (!!obj && obj.properties) {
            Object.keys(obj.properties).forEach((key: string) => {
                if (obj.properties[key].type == 'string') {
                    flatProperties.push(Object.assign(obj.properties[key], {name: key}));
                } else {
                    flatProperties = flatProperties.concat(...this.getPropertiesFromComplexObject(obj.properties[key].$ref));
                }
            })
        }

        return flatProperties;
    }


    private getObjectFromModel(name: string) {
        return this.apiState.apiRequestModels[name]
    }




}
