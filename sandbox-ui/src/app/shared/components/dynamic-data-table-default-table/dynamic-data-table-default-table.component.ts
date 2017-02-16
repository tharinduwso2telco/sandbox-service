import {Component, OnInit, Input, Injector, Output, EventEmitter} from '@angular/core';
import {UserNumber} from "../../../data-store/models/manage-numbers-model";
import {
    RowAction, RowActionEvent, ROW_EDIT_ACTION,
    ROW_DELETE_ACTION
} from "../../../data-store/models/responsive-table-models";


@Component({
    selector: 'dynamic-data-table-default-table',
    templateUrl: './dynamic-data-table-default-table.component.html',
    styleUrls: ['./dynamic-data-table-default-table.component.scss']
})
export class DynamicDataTableDefaultTableComponent implements OnInit {

    @Input()
    private tableDataSource: UserNumber[];

    @Input()
    private fieldNames: string[];

    @Output()
    private onTableRowAction:EventEmitter<RowActionEvent> = new EventEmitter();

    private rowDataActions:RowAction[] = [
        {
            id: ROW_EDIT_ACTION,
            name: 'Edit',
            icon: 'border_color',
            class: 'row-edit'
        },
        {
            id: ROW_DELETE_ACTION,
            name: 'Delete',
            icon: 'delete',
            class: 'row-del'
        }
        ];

    constructor(private injector: Injector) {
    }

    ngOnInit() {
        this.tableDataSource = this.injector.get('tableDataSource');
        this.fieldNames = this.injector.get('fieldNames');
    }

}
