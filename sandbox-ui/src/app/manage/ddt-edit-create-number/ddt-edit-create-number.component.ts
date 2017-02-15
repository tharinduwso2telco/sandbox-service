import {Component, OnInit} from '@angular/core';
import {IAppState, IManageNumberState} from "../../data-store/models/common-models";
import {Store} from "@ngrx/store";
import {UserNumber} from "../../data-store/models/manage-numbers-model";
import {ManageActionCreatorService} from "../../data-store/actions/manage-action-creator.service";

@Component({
    selector: 'ddt-edit-create-number',
    templateUrl: './ddt-edit-create-number.component.html',
    styleUrls: ['./ddt-edit-create-number.component.scss']
})
export class DDTEditCreateNumberComponent implements OnInit {

    private numberModel: UserNumber;

    constructor(private store: Store<IAppState>,
                private manageActionCreator: ManageActionCreatorService) {
    }

    ngOnInit() {
        this.store.select('ManageNumber')
            .subscribe((manNumberState: IManageNumberState) => {
                this.numberModel = manNumberState.selectedNumber;

                if (!this.numberModel) {
                    this.numberModel = new UserNumber();
                }
            })
    }

    onFormSubmit() {
        this.manageActionCreator.addUserNumber(this.numberModel);
    }

}
