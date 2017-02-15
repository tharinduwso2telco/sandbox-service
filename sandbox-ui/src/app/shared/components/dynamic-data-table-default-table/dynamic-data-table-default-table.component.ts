import {Component, OnInit, Input, Injector} from '@angular/core';
import {UserNumber} from "../../../data-store/models/manage-numbers-model";


@Component({
    selector: 'dynamic-data-table-default-table',
    templateUrl: './dynamic-data-table-default-table.component.html',
    styleUrls: ['./dynamic-data-table-default-table.component.scss']
})
export class DynamicDataTableDefaultTableComponent implements OnInit {

    @Input()
    private tableDataSource:UserNumber[];

    @Input()
    private fieldNames:string[];

    constructor(private injector:Injector) {
    }

    ngOnInit() {
        this.tableDataSource = this.injector.get('tableDataSource');
        this.fieldNames = this.injector.get('fieldNames');
    }
}
