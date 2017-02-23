import {Observable} from "rxjs";
export const FORM_CONTROL_TYPES:any = {
    TEXT_INPUT :'TEXT_INPUT',
    TEXT_AREA : 'TEXT_AREA',
    CHECKBOX_INPUT : 'CHECKBOX_INPUT',
    RADIO_INPUT : 'RADIO_INPUT',
    DROPDOWN : 'DROPDOWN'
};

export class FormItemBase<T> {
    formControlType: string;
    key: string;
    value: T;
    label: string;
    required : boolean
    validators: any[];
    order: number;
    type: string;


    constructor(options: {
        formControlType?: 'TEXT_INPUT' | 'TEXT_AREA' | 'CHECKBOX_INPUT' | 'RADIO_INPUT' | 'DROPDOWN';
        type?: string,
        key?: string,
        value?: T,
        label?: string,
        required?:boolean,
        validators?: any[],
        order?: number
    }) {

        this.formControlType = options.formControlType;
        this.key = options.key;
        this.value = options.value;
        this.label = options.label;
        this.required = options.required;
        this.validators = options.validators;
        this.order = options.order;
        this.type = options.type;

    }
}


export class TextInputFormItem extends FormItemBase<string> {

    formControlType = FORM_CONTROL_TYPES.TEXT_INPUT;

    constructor(options: {} = {}) {
        super(options);

        this.type = options['type'] || 'text';
    }
}

export class TextAreaFormItem extends FormItemBase<string> {

    formControlType = FORM_CONTROL_TYPES.TEXT_AREA;

    constructor(options: {} = {}) {
        super(options);
    }
}

export class DropDownControl extends  FormItemBase<string>{
    formControlType = FORM_CONTROL_TYPES.DROPDOWN;
    dropDownOptions: Observable<any>;

    constructor(options:{}={}){
        super(options);

        this.dropDownOptions = options['dropDownOptions'] || [];
    }
}




