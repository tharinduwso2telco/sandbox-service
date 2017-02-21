import {Injectable} from '@angular/core';
import {IAppState, IApiState, ApiCategory} from "../data-store/models/common-models";
import {Store} from "@ngrx/store";
import {ApiRemoteService} from "../data-store/services/api-remote-service";

@Injectable()
export class ApiHelperService {

    constructor(private store: Store<IAppState>,
                ) {

    }
}
