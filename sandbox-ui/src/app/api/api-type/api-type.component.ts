import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from "@angular/router";
import {IAppState, IApiState} from "../../data-store/models/common-models";
import {Store} from "@ngrx/store";

@Component({
    selector: 'api-type',
    templateUrl: './api-type.component.html',
    styleUrls: ['./api-type.component.scss']
})
export class ApiTypeComponent implements OnInit {

    private serviceTypes: string[];

    private apiType:string;

    constructor(private store: Store<IAppState>,
                private router: Router,
                private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.store.select('ApiData')
            .subscribe((apiState: IApiState) => {
                this.serviceTypes = apiState.serviceTypes;
            });

        this.activatedRoute.params.subscribe((param:any)=>{
            this.apiType = param.apiType;
        });

    }

    onServiceClick(service) {
        this.router.navigate([service], {relativeTo: this.activatedRoute});
    }
}
