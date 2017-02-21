import {Injectable} from '@angular/core';
import {IAppState, IApiState, ApiCategory, Api, Operation} from "../data-store/models/common-models";
import {Store} from "@ngrx/store";
import {ApiRemoteService} from "../data-store/services/api-remote-service";
import {FormItemBase} from "../data-store/models/form-models";

@Injectable()
export class ApiHelperService {

    constructor() {
    }

    getFormModelForApi(api: Api) {
        let formModel: FormItemBase<any>[] = [];
        let operation: Operation = api.operations[0]; // only extract first operation. extend if need be
        if (!!operation) {
            operation.parameters.forEach(()=>{

            });
        }
        return formModel;
    }

}
