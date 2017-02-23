import {Component, OnInit, Input, OnChanges, SimpleChanges} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {IAppState} from "../../../data-store/models/common-models";
import {Store} from "@ngrx/store";
import {Observable} from "rxjs";

@Component({
    selector: 'dynamic-form-element',
    templateUrl: './dynamic-form-element.component.html',
    styleUrls: ['./dynamic-form-element.component.scss']
})
export class DynamicFormElementComponent implements OnChanges {

    @Input()
    private elementData: any;

    @Input()
    private form: FormGroup;

    private dropdownSource:any[];

    constructor() {
    }


    ngOnChanges(changes: SimpleChanges): void {
            if(changes['elementData'] && changes['elementData'].currentValue && changes['elementData'].currentValue['dropDownOptions']){
                (changes['elementData'].currentValue['dropDownOptions'] as Observable<any>)
                    .subscribe((source)=>{
                        this.dropdownSource = source;
                    })
            }
    }
}
