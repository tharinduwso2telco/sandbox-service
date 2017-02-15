import {Component, OnInit, ElementRef, Renderer} from '@angular/core';
import {DynamicComponentData, IAppState, IManageNumberState} from "../../data-store/models/common-models";
import {DynamicDataTableDefaultHeaderComponent} from "../../shared/components/dynamic-data-table-default-header/dynamic-data-table-default-header.component";
import {DynamicDataTableDefaultTableComponent} from "../../shared/components/dynamic-data-table-default-table/dynamic-data-table-default-table.component";
import {DynamicDataTableDefaultEditorComponent} from "../../shared/components/dynamic-data-table-default-editor/dynamic-data-table-default-editor.component";
import {ManageActionCreatorService} from "../../data-store/actions/manage-action-creator.service";
import {Store} from "@ngrx/store";

@Component({
    selector: 'numbers',
    templateUrl: './numbers.component.html',
    styleUrls: ['./numbers.component.scss']
})
export class NumbersComponent implements OnInit {

    constructor(private manageActionCreator: ManageActionCreatorService,
                private store: Store<IAppState>) {
    }

    ngOnInit() {
        this.store.select('ManageNumber')
            .subscribe((manNumState:IManageNumberState)=>{
                this.isEditorOpen = manNumState.isEditorPanelOpen;
            });
    }

    private isEditorOpen:boolean;

    private tmpData = [
        {
            Number: '9471933270',
            description: 'Test User',
            Balance: 100,
            reserveAmount: 50

        },
        {
            Number: '9471933270',
            description: 'Test User',
            Balance: 100,
            reserveAmount: 50

        }
        , {
            Number: '9471933270',
            description: 'Test User',
            Balance: 100,
            reserveAmount: 50

        }
        , {
            Number: '9471933270',
            description: 'Test User',
            Balance: 100,
            reserveAmount: 50

        }
    ];

    private fields: string[] = ['Number', 'description', 'Balance', 'reserveAmount'];

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

    private tableData = {
        component: DynamicDataTableDefaultTableComponent,
        inputData: {}
    };
    private editorData = {
        component: DynamicDataTableDefaultEditorComponent,
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
