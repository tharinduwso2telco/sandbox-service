import {Injectable} from '@angular/core';
import {FormItemBase} from "../../data-store/models/form-models";
import {FormControl, Validators, FormGroup} from "@angular/forms";

@Injectable()
export class FormGeneratorService {

    constructor() {
    }

    getForm(formItems: FormItemBase<any>[]) {
        let group = {};
        formItems.forEach((item: FormItemBase<any>) => {
            group[item.key] = item.validators ? new FormControl(item.value || '', item.validators) : new FormControl(item.value || '');
        });

        return new FormGroup(group);
    }
}
