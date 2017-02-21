import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from "@angular/router";
import {IAppState, IApiState, ServiceTypeCategory} from "../../data-store/models/common-models";
import {Store} from "@ngrx/store";
import {ApiRemoteService} from "../../data-store/services/api-remote-service";

@Component({
    selector: 'api-type',
    templateUrl: './api-type.component.html',
    styleUrls: ['./api-type.component.scss']
})
export class ApiTypeComponent implements OnInit {

    private serviceTypes: ServiceTypeCategory[];

    private apiType: string;

    constructor(private store: Store<IAppState>,
                private router: Router,
                private apiRemoteService: ApiRemoteService,
                private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.store.select('ApiData')
            .subscribe((apiState: IApiState) => {
                this.serviceTypes = apiState.serviceTypes;
            });
    }

    onServiceClick(service) {
         this.router.navigate([service.name], {relativeTo: this.activatedRoute});
    }
}
