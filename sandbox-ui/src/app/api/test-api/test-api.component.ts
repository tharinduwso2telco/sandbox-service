import {Component, OnInit, AfterViewChecked} from '@angular/core';
import {FormItemBase, TextInputFormItem} from "../../data-store/models/form-models";
import {Validators, FormGroup} from "@angular/forms";
import {MsisdnValidator} from "../../shared/validators/msisdn-validator";
import {Store} from "@ngrx/store";
import {
    IAppState, IApiState, Api, ServiceConfig, Parameter,
    IManageNumberState
} from "../../data-store/models/common-models";
import {ApiHelperService} from "../api-helper.service";
import {setFlagsFromString} from "v8";
import {ManageRemoteService} from "../../data-store/services/manage-remote.service";


@Component({
    selector: 'test-api',
    templateUrl: './test-api.component.html',
    styleUrls: ['./test-api.component.scss']
})
export class TestApiComponent implements OnInit {

    private formDataModel: FormItemBase<any>[] = [];
    private serviceConfig: ServiceConfig;
    private selectedApi: Api;

    constructor(private store: Store<IAppState>,
                private apiHelper: ApiHelperService,
                private manageRemoteService: ManageRemoteService) {
    }

    ngOnInit() {
        this.store.select('ApiData')
            .subscribe((apiData: IApiState) => {
                this.serviceConfig = apiData.selectedApiConfig;
                this.setFormData(this.serviceConfig);
            });

        this.store.select('ManageNumber')
            .subscribe((manNumber: IManageNumberState) => {
                if (manNumber && manNumber.numbersTableData && manNumber.numbersTableData.inputData && manNumber.numbersTableData.inputData.tableDataSource) {
                    let data = manNumber.numbersTableData.inputData.tableDataSource.map((d) => {
                        return {
                            key: d.number,
                            value: d.number
                        }
                    });
                    this.setFormData(this.serviceConfig, data);
                } else {
                    this.manageRemoteService.getUserNumbers();
                }
            });
    }

    private setFormData(config: ServiceConfig, data ?: any) {
        if (!!config) {
            this.selectedApi = this.apiHelper.getApiFromConfig(this.serviceConfig);

            var params: Parameter = this.selectedApi.operations[0].parameters[0];

            if (data) {
                params.misc = {
                    dropDownOptions: data
                };
            }

            this.formDataModel = this.apiHelper.getFormModelForApi(this.selectedApi,config);
        }
    }

    onFormInitialize(form: FormGroup) {
        console.log(form)
    }

    onFormSubmit(formVal) {
        this.apiHelper.dynamicFormSubmit(formVal, this.selectedApi);
    }
}
