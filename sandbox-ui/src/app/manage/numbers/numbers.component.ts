import {Component, OnInit, ElementRef, Renderer} from '@angular/core';
import {
    DynamicComponentData, IAppState, IManageNumberState,
    IApplicationData
} from "../../data-store/models/common-models";
import {DynamicDataTableDefaultHeaderComponent} from "../../shared/components/dynamic-data-table-default-header/dynamic-data-table-default-header.component";
import {DynamicDataTableDefaultTableComponent} from "../../shared/components/dynamic-data-table-default-table/dynamic-data-table-default-table.component";
import {DynamicDataTableDefaultEditorComponent} from "../../shared/components/dynamic-data-table-default-editor/dynamic-data-table-default-editor.component";
import {ManageActionCreatorService} from "../../data-store/actions/manage-action-creator.service";
import {Store} from "@ngrx/store";
import {ManageRemoteService} from "../../data-store/services/manage-remote.service";
import {DDTEditCreateNumberComponent} from "../ddt-edit-create-number/ddt-edit-create-number.component";

@Component({
    selector: 'numbers',
    templateUrl: './numbers.component.html',
    styleUrls: ['./numbers.component.scss']
})
export class NumbersComponent implements OnInit {

    constructor(private manageActionCreator: ManageActionCreatorService,
                private manageRemoteService: ManageRemoteService,
                private store: Store<IAppState>) {
    }

    ngOnInit() {
        this.store.select('ManageNumber')
            .subscribe((manNumState: IManageNumberState) => {
                this.isEditorOpen = manNumState.isEditorPanelOpen;
                this.tableData  = manNumState.numbersTableData;
            });

        this.store.select('AppData')
            .subscribe(
                (appData: IApplicationData) => {
                    if (appData.isReloadTriggered) {
                        this.manageRemoteService.getUserNumbers();
                    }
                });

        this.manageRemoteService.getUserNumbers();
    }

    private isEditorOpen: boolean;

    private headerData: DynamicComponentData = {
        component: DynamicDataTableDefaultHeaderComponent,
        inputData: {
            buttonCol: [
                {
                    id: 1,
                    name: 'Add',
                    icon: ''
                }
            ]
        },
        eventBinding: [
            {
                eventName: 'onButtonClick',
                subscriber: this.onTopBarButtonClick,
                context: this
            }
        ]
    };

    private tableData:any;

    private editorData = {
        component: DDTEditCreateNumberComponent,
        inputData: {}
    };


    onTopBarButtonClick(button: any) {

        if (!!button) {
            if (button.id == 1) {
                this.manageActionCreator.openAddNumber();
            }
        }
    }

}
