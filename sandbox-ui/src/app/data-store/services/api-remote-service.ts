import {Injectable, Inject} from '@angular/core';
import {Headers, RequestOptions, Http, Response} from "@angular/http";
import {Store} from "@ngrx/store";
import {IAppState} from "../models/common-models";
import {MessageService} from "../../shared/services/message.service";
import {ApiActionCreatorService} from "../actions/api-action-creator.service";


@Injectable()
export class ApiRemoteService {

    constructor(private http: Http,
                private apiActionCreator: ApiActionCreatorService,
                private message: MessageService,
                @Inject('SANDBOX_SERVER_PROXY_PATTERN') private sandboxPattern: string) {
    }

    private headers: Headers = new Headers(
        {
            'Content-Type': 'application/json',
            'sandbox': 'admin'
        }
    );

    private options: RequestOptions = new RequestOptions({headers: this.headers});


    getApiTypes() {
        this.http.get(this.sandboxPattern + '/user/apiType', this.options)
            .map((response: Response) => response.json())
            .subscribe((response) => {
                this.apiActionCreator.updateApiTypes(response.apiTypes);
            })
    }

    getApiServiceTypes(apiType: string) {
        if (!!apiType) {
            this.http.get(this.sandboxPattern + `/user/${apiType.toLowerCase()}/serviceType`)
                .map((response: Response) => response.json())
                .subscribe((response) => {
                        this.apiActionCreator.setApiServiceTypes(response.apiServiceCallTypes);
                    },
                    () => {
                        this.apiActionCreator.setApiServiceTypes(null);
                    })
        }

    }

}
