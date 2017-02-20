import {Component, OnInit, Input, OnChanges, SimpleChanges} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
    selector: 'dynamic-form-element',
    templateUrl: './dynamic-form-element.component.html',
    styleUrls: ['./dynamic-form-element.component.scss']
})
export class DynamicFormElementComponent {

    @Input()
    private elementData: any;

    @Input()
    private form: FormGroup;

    constructor() {
    }


}
