import {Injectable} from '@angular/core';
import {IAppState, IApiState, ApiCategory, Api, Operation, Parameter} from "../data-store/models/common-models";
import {Store} from "@ngrx/store";
import {ApiRemoteService} from "../data-store/services/api-remote-service";
import {FormItemBase, TextInputFormItem} from "../data-store/models/form-models";
import {Validators} from "@angular/forms";

@Injectable()
export class ApiHelperService {

    private apiState: IApiState;

    constructor(private store: Store<IAppState>) {
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
                        if(properties.length>0){
                            properties.forEach((prp,i)=>{
                                formModel.push(this.getTextInput(prp, index+i));
                            })
                        }
                        console.log(properties);
                    }
                }
            });
        }
        return formModel;
    }


    private getTextInput(param: Parameter, index: number) {
        let tmp: FormItemBase<any> = new TextInputFormItem({
            key: param.name,
            label: param.description || param.name,
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

    private getPropertiesFromComplexObject(name:string){
        let flatProperties = [];
        let obj = this.getObjectFromModel(name);
        if(!!obj && obj.properties){
            Object.keys(obj.properties).forEach((key:string)=>{
                if(obj.properties[key].type == 'string'){
                    flatProperties.push(Object.assign(obj.properties[key],{name:key}));
                }else{
                    flatProperties = flatProperties.concat(...this.getPropertiesFromComplexObject(obj.properties[key].$ref));
                }
            })
        }

        return flatProperties;
    }


    private getObjectFromModel(name:string){
        return this.apiState.apiRequestModels[name]
    }
}
