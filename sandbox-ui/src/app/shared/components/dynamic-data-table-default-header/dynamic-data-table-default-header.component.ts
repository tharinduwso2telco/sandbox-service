import {Component, OnInit, Injector, Output, EventEmitter} from '@angular/core';
import {NumberButtons} from "../../../data-store/models/manage-numbers-model";

@Component({
    selector: 'dynamic-data-table-default-header',
    templateUrl: './dynamic-data-table-default-header.component.html',
    styleUrls: ['./dynamic-data-table-default-header.component.scss']
})
export class DynamicDataTableDefaultHeaderComponent implements OnInit {

    buttonCol: any[];

    @Output()
    public onButtonClick:EventEmitter<NumberButtons> = new EventEmitter<NumberButtons>();

    constructor(private injector: Injector) {
    }

    ngOnInit() {
        this.buttonCol = this.injector.get('buttonCol');
    }
}
