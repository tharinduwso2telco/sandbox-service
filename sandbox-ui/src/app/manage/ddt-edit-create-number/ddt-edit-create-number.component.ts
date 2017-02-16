import {Component, OnInit} from '@angular/core';
import {IAppState, IManageNumberState} from "../../data-store/models/common-models";
import {Store} from "@ngrx/store";
import {UserNumber} from "../../data-store/models/manage-numbers-model";
import {ManageRemoteService} from "../../data-store/services/manage-remote.service";
import {ManageActionCreatorService} from "../../data-store/actions/manage-action-creator.service";

@Component({
    selector: 'ddt-edit-create-number',
    templateUrl: './ddt-edit-create-number.component.html',
    styleUrls: ['./ddt-edit-create-number.component.scss']
})
export class DDTEditCreateNumberComponent implements OnInit {

    private numberModel: UserNumber = new UserNumber();

    constructor(private store: Store<IAppState>,
                private manageActionCreator:ManageActionCreatorService,
                private manageRemoteService: ManageRemoteService) {
    }

    ngOnInit() {
        this.store.select('ManageNumber')
            .subscribe((manNumberState: IManageNumberState) => {
                this.numberModel = manNumberState.selectedNumber;
            })
    }

    onFormSubmit() {
        this.manageRemoteService.addUserNumber(this.numberModel);
    }

    onClose(){
        this.manageActionCreator.closeEditorPanel();
    }

}
