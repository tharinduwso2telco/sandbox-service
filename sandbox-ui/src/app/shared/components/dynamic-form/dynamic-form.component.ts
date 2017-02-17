import {Component, OnInit, Input} from '@angular/core';
import {FormItemBase} from "../../../data-store/models/form-models";
import {FormGroup} from "@angular/forms";
import {FormGeneratorService} from "../../services/form-generator.service";

@Component({
    selector: 'dynamic-form',
    templateUrl: './dynamic-form.component.html',
    styleUrls: ['./dynamic-form.component.scss']
})
export class DynamicFormComponent implements OnInit {

    @Input()
    private formItems: FormItemBase<any>[];

    private dynamicForm: FormGroup;

    constructor(private formService:FormGeneratorService) {
    }

    ngOnInit() {
        this.dynamicForm = this.formService.getForm(this.formItems);
    }

    onFormSubmit() {
        alert(JSON.stringify(this.dynamicForm.value));
    }

}
