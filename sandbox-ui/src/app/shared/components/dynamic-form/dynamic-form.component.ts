import {
    Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges
} from '@angular/core';
import {FormItemBase} from "../../../data-store/models/form-models";
import {FormGroup} from "@angular/forms";
import {FormGeneratorService} from "../../services/form-generator.service";

@Component({
    selector: 'dynamic-form',
    templateUrl: './dynamic-form.component.html',
    styleUrls: ['./dynamic-form.component.scss']
})
export class DynamicFormComponent implements OnInit,OnChanges {

    @Input()
    private formItems: FormItemBase<any>[];

    @Input()
    private formLayoutGridStyle: string = '';

    @Output()
    private onFormSubmit: EventEmitter<any> = new EventEmitter();

    @Output()
    private onFormInitialize: EventEmitter<FormGroup> = new EventEmitter();

    private dynamicForm: FormGroup;

    constructor(private formService: FormGeneratorService) {
    }

    ngOnInit() {
        this.onFormInitialize.emit(this.dynamicForm);
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.dynamicForm = this.formService.getForm(this.formItems);
    }


}
