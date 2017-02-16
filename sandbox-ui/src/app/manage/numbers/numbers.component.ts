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
import {
    RowActionEvent, ROW_SELECTED, ROW_EDIT_ACTION,
    ROW_DELETE_ACTION
} from "../../data-store/models/responsive-table-models";

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

                let tmpTableData = Object.assign({}, manNumState.numbersTableData, {
                    eventBinding: [
                        {
                            eventName: 'onTableRowAction',
                            subscriber: this.onTableRowActionListener,
                            context: this
                        }
                    ]
                });

                this.tableData = tmpTableData;
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

    private tableData: any;
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


    private editorData = {
        component: DDTEditCreateNumberComponent,
        inputData: {}
    };


    onTopBarButtonClick(button: any) {

        if (!!button) {
            if (button.id == 1) {
                this.manageActionCreator.addNumber();
            }
        }
    }

    onTableRowActionListener(item: RowActionEvent) {
        if(!!item){
            switch (item.action.id){
                case ROW_SELECTED : {
                    this.manageActionCreator.editNumber(item.rowData);
                    break;
                }

                case ROW_EDIT_ACTION :{
                    this.manageActionCreator.editNumber(item.rowData);
                    break;
                }

                case ROW_DELETE_ACTION :{
                    this.manageRemoteService.deleteNumber(item.rowData);
                    break;
                }
            }

        }
    }

}
