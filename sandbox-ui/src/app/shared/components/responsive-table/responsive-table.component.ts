import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import {ResponsiveTableHelperService} from "../../services/responsive-table-helper.service";
import {RowAction, RowActionEvent, ROW_SELECTED} from "../../../data-store/models/responsive-table-models";

@Component({
    selector: 'responsive-table',
    templateUrl: './responsive-table.component.html',
    styleUrls: ['./responsive-table.component.scss']
})
export class ResponsiveTableComponent implements OnInit {

    @Input()
    private tableHeader: string;

    @Input()
    private dataSource: any[];

    @Input()
    private fieldSet: string[];

    @Input()
    private rowActions: RowAction[];

    @Output()
    private onRowAction: EventEmitter<RowActionEvent> = new EventEmitter();

    constructor(private helper: ResponsiveTableHelperService) {
    }

    ngOnInit() {
    }

    onRowClick(item: any) {
        this.helper.selectedRow = item;

        let t = new RowActionEvent();
        t.action = new RowAction();
        t.action.id = ROW_SELECTED;
        t.rowData = item;

        this.onRowAction.emit(t);
    }

    onIconClick(action: RowAction, rowData: any, event: MouseEvent) {
        let t = new RowActionEvent();
        t.action = action;
        t.rowData = rowData;

        this.onRowAction.emit(t)
        event.preventDefault();
        event.stopPropagation();
    }

}
