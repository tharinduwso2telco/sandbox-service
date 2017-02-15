import {Component, OnInit, Input, Injector} from '@angular/core';
import {IUserNumber} from "../../../data-store/models/common-models";

@Component({
    selector: 'dynamic-data-table-default-table',
    templateUrl: './dynamic-data-table-default-table.component.html',
    styleUrls: ['./dynamic-data-table-default-table.component.scss']
})
export class DynamicDataTableDefaultTableComponent implements OnInit {

    @Input()
    private tableDataSource:IUserNumber[];

    @Input()
    private fieldNames:string[];

    constructor(private injector:Injector) {
    }

    ngOnInit() {
        this.tableDataSource = this.injector.get('tableDataSource');
        this.fieldNames = this.injector.get('fieldNames');
    }

    /*private tmpData = [
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
    ];*/

  /*  private fields: string[] = ['Number', 'description', 'Balance', 'reserveAmount'];*/



}
