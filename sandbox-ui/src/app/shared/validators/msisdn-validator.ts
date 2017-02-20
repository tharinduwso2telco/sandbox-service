import {ValidatorFn, AbstractControl} from "@angular/forms";


export function MsisdnValidator():ValidatorFn{
    let msisdnRegex:RegExp = new RegExp(/^(\d)[0-9]{9}$/);

    return (control: AbstractControl): {[key: string]: any} => {
        const fieldVal = control.value;
        const isValidMsisdn = msisdnRegex.test(fieldVal);
        return isValidMsisdn ? null : {'invalid_Format': {name}};
    };
}