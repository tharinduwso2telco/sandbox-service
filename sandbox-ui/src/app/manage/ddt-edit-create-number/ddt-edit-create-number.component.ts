import {Component, OnInit} from '@angular/core';
import {IAppState, IManageNumberState} from "../../data-store/models/common-models";
import {Store} from "@ngrx/store";
import {UserNumber} from "../../data-store/models/manage-numbers-model";
import {ManageRemoteService} from "../../data-store/services/manage-remote.service";
import {ManageActionCreatorService} from "../../data-store/actions/manage-action-creator.service";
import {
    FormItemBase, TextInputFormItem, FORM_CONTROL_TYPES,
    TextAreaFormItem
} from "../../data-store/models/form-models";
import {FormGroup, Validators} from "@angular/forms";
import {MsisdnValidator} from "../../shared/validators/msisdn-validator";
import {RegexValidator} from "../../shared/validators/regex-validator";

@Component({
    selector: 'ddt-edit-create-number',
    templateUrl: './ddt-edit-create-number.component.html',
    styleUrls: ['./ddt-edit-create-number.component.scss']
})
export class DDTEditCreateNumberComponent implements OnInit {

    private userNumber: UserNumber;

    constructor(private store: Store<IAppState>,
                private manageActionCreator: ManageActionCreatorService,
                private manageRemoteService: ManageRemoteService) {
    }

    private formDataModel: FormItemBase<any>[];

    ngOnInit() {
        this.store.select('ManageNumber')
            .subscribe((manNumberState: IManageNumberState) => {
                this.userNumber = manNumberState.selectedNumber;
                this.formDataModel = this.getFormDataModel(this.userNumber);
            })
    }

    onFormSubmit(formVal: any) {
        let tmpNumber = Object.assign({},this.userNumber,formVal);
        this.manageRemoteService.addUserNumber(tmpNumber);
    }

    onClose() {
        this.manageActionCreator.closeEditorPanel();
    }

    private getFormDataModel(selectedNumber: UserNumber) {
        return [
            new TextInputFormItem({
                key: 'number',
                label: 'Number',
                order: 1,
                value: selectedNumber ? (selectedNumber.number || '') : '',
                validators: [
                    Validators.required,
                    MsisdnValidator()
                ]
            }),
            new TextInputFormItem({
                key: 'numberBalance',
                label: 'Balance',
                value: selectedNumber ? (selectedNumber.numberBalance || '') : '',
                validators : [
                    RegexValidator(/^$|[0-9]/)
                ],
                order: 2
            }),
            new TextInputFormItem(
                {
                    key: 'reservedAmount',
                    label: 'Reserved Amount',
                    value: selectedNumber ? (selectedNumber.reservedAmount || '') : '',
                    order: 3,
                    validators : [
                        RegexValidator(/^$|[0-9]/)
                    ],
                }
            ),
            new TextAreaFormItem(
                {
                    key: 'description',
                    type: 'text',
                    value: selectedNumber ? (selectedNumber.description || '') : '',
                    label: 'Description',
                    order: 4
                }
            )
        ];
    }

}
