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

    private formDataModel: FormItemBase<any>[];
    private selectedApi: Api;

    constructor(private store: Store<IAppState>,
                private apiHelper: ApiHelperService) {
    }

    ngOnInit() {
        this.store.select('ApiData')
            .subscribe((apiData: IApiState) => {
                this.selectedApi = apiData.selectedApi;
                this.formDataModel = this.apiHelper.getFormModelForApi(this.selectedApi);
            });
    }

    onFormSubmit(formVal) {
        alert(formVal)
    }

    private
    getFormDataModel() {
        return [
            new TextInputFormItem({
                key: 'endUserId',
                label: 'End User Id',
                order: 1,
                value: '',
                required: true,
                validators: [
                    Validators.required,
                    MsisdnValidator()
                ]
            }),
            new TextInputFormItem({
                key: 'amount',
                label: 'Amount',
                value: '',
                required: true,
                validators: [
                    Validators.required,
                ],
                order: 2
            }),
            new TextInputFormItem({
                key: 'currency',
                label: 'Currency',
                value: '',
                required: true,
                validators: [
                    Validators.required,
                ],
                order: 3
            }),
            new TextInputFormItem({
                key: 'description',
                label: 'Description',
                value: '',
                required: true,
                validators: [
                    Validators.required,
                ],
                order: 4
            }),
            new TextInputFormItem({
                key: 'referenceCode',
                label: 'Reference Code',
                value: '',
                required: true,
                validators: [
                    Validators.required,
                ],
                order: 5
            }),
            new TextInputFormItem({
                key: 'notifyURL',
                label: 'Notify URL',
                order: 6,
                value: '',
                validators: []
            }),
            new TextInputFormItem(
                {
                    key: 'onBehalfOf',
                    label: 'On Behalf Of',
                    value: '',
                    order: 7,
                    validators: [],
                }
            ),
            new TextInputFormItem(
                {
                    key: 'purchaseCategoryCode',
                    label: 'Purchase Category Code',
                    value: '',
                    order: 7,
                    validators: [],
                }
            ),
            new TextInputFormItem(
                {
                    key: 'channel',
                    label: 'Channel',
                    value: '',
                    order: 7,
                    validators: [],
                }
            )

        ];

    }
}
