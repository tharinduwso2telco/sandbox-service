import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'dynamic-data-table-default-table',
    templateUrl: './dynamic-data-table-default-table.component.html',
    styleUrls: ['./dynamic-data-table-default-table.component.scss']
})
export class DynamicDataTableDefaultTableComponent implements OnInit {

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

    constructor() {
    }

    ngOnInit() {
    }

}
