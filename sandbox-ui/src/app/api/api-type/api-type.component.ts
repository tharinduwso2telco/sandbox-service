import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from "@angular/router";
import {IAppState, IApiState, ServiceTypeCategory} from "../../data-store/models/common-models";
import {Store} from "@ngrx/store";
import {ApiRemoteService} from "../../data-store/services/api-remote-service";
import {ApiActionCreatorService} from "../../data-store/actions/api-action-creator.service";

@Component({
    selector: 'api-type',
    templateUrl: './api-type.component.html',
    styleUrls: ['./api-type.component.scss']
})
export class ApiTypeComponent implements OnInit {

    private serviceTypes: ServiceTypeCategory[];

    constructor(private store: Store<IAppState>,
                private router: Router,
                private actionCreator:ApiActionCreatorService,
                private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.store.select('ApiData')
            .subscribe((apiState: IApiState) => {
                this.serviceTypes = apiState.serviceTypes;
            });
    }

    onServiceClick(apiEndpoint) {
        this.actionCreator.setSelectedApi(apiEndpoint);
        this.router.navigate([apiEndpoint.name], {relativeTo: this.activatedRoute});
    }
}
