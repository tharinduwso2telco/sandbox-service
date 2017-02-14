import {Component, OnInit} from '@angular/core';
import {DynamicComponentData} from "../../data-store/models/common-models";
import {DynamicDataTableDefaultHeaderComponent} from "../../shared/components/dynamic-data-table-default-header/dynamic-data-table-default-header.component";
import {DynamicDataTableDefaultTableComponent} from "../../shared/components/dynamic-data-table-default-table/dynamic-data-table-default-table.component";
import {DynamicDataTableDefaultEditorComponent} from "../../shared/components/dynamic-data-table-default-editor/dynamic-data-table-default-editor.component";

@Component({
    selector: 'numbers',
    templateUrl: './numbers.component.html',
    styleUrls: ['./numbers.component.scss']
})
export class NumbersComponent implements OnInit {

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
        inputData: {}
    };

    private tableData = {
        component: DynamicDataTableDefaultTableComponent,
        inputData: {}
    };
    private editorData = {
        component: DynamicDataTableDefaultEditorComponent,
        inputData: {}
    };

    constructor() {
    }

    ngOnInit() {
    }

}
