import {ValidatorFn, AbstractControl} from "@angular/forms";

export function RegexValidator(regex:RegExp){
    if(!!regex){
        return (control: AbstractControl): {[key: string]: any} => {
            const fieldVal = control.value;
            const isValidMsisdn = regex.test(fieldVal);
            return isValidMsisdn ? null : {'invalid_Format': {name}};
        };
    }
}