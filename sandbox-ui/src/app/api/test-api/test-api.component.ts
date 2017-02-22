import {Component, OnInit} from '@angular/core';
import {FormItemBase, TextInputFormItem} from "../../data-store/models/form-models";
import {Validators} from "@angular/forms";
import {MsisdnValidator} from "../../shared/validators/msisdn-validator";
import {Store} from "@ngrx/store";
import {IAppState, IApiState, Api} from "../../data-store/models/common-models";
import {ApiHelperService} from "../api-helper.service";


@Component({
    selector: 'test-api',
    templateUrl: './test-api.component.html',
    styleUrls: ['./test-api.component.scss']
})
export class TestApiComponent implements OnInit {

    private formDataModel: FormItemBase<any>[] = [];
    private selectedApi: Api;

    constructor(private store: Store<IAppState>,
                private apiHelper: ApiHelperService) {
    }

    ngOnInit() {
        this.store.select('ApiData')
            .subscribe((apiData: IApiState) => {
                this.selectedApi = apiData.selectedApi;
                if(!!this.selectedApi){
                    this.formDataModel = this.apiHelper.getFormModelForApi(this.selectedApi);
                }
            });
    }

    onFormSubmit(formVal) {
        this.apiHelper.dynamicFormSubmit(formVal,this.selectedApi);
    }
}
