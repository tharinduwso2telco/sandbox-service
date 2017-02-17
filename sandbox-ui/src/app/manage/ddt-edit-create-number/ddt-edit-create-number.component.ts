import {Component, OnInit} from '@angular/core';
import {IAppState, IManageNumberState} from "../../data-store/models/common-models";
import {Store} from "@ngrx/store";
import {UserNumber} from "../../data-store/models/manage-numbers-model";
import {ManageRemoteService} from "../../data-store/services/manage-remote.service";
import {ManageActionCreatorService} from "../../data-store/actions/manage-action-creator.service";
import {FormItemBase, TextInputFormItem,FORM_CONTROL_TYPES} from "../../data-store/models/form-models";

@Component({
    selector: 'ddt-edit-create-number',
    templateUrl: './ddt-edit-create-number.component.html',
    styleUrls: ['./ddt-edit-create-number.component.scss']
})
export class DDTEditCreateNumberComponent implements OnInit {

    private numberModel: UserNumber = new UserNumber();

    constructor(private store: Store<IAppState>,
                private manageActionCreator: ManageActionCreatorService,
                private manageRemoteService: ManageRemoteService) {
    }

    private formDataModel: FormItemBase<any>[] = [
        new TextInputFormItem({
            key: 'number',
            label: 'Number',
            order: 1,
            required: true
        }),
        new TextInputFormItem({
            key: 'balance',
            label: 'Balance',
            order: 2
        }),
        new TextInputFormItem(
            {
                key: 'reserveAmount',
                label: 'Reserved Amount',
                order: 3
            }
        ),
        new TextInputFormItem(
            {
                formControlType: FORM_CONTROL_TYPES.TEXT_AREA,
                key: 'description',
                type: 'text',
                label: 'Description',
                order: 4
            }
        )
    ];

    ngOnInit() {
        this.store.select('ManageNumber')
            .subscribe((manNumberState: IManageNumberState) => {
                this.numberModel = manNumberState.selectedNumber;
            })
    }

    onFormSubmit() {
        this.manageRemoteService.addUserNumber(this.numberModel);
    }

    onClose() {
        this.manageActionCreator.closeEditorPanel();
    }

}
