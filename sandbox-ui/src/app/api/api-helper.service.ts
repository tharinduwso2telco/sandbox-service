import {Injectable} from '@angular/core';
import {IAppState} from "../data-store/models/common-models";
import {Store} from "@ngrx/store";

@Injectable()
export class ApiHelperService {

    private apiValidChildRoutes = ['wallet'];

    constructor() {

    }

    isValidChildRoute(route) {
        //TODO Find a better mechanism to find valid child routes
        return this.apiValidChildRoutes.indexOf(route) >= 0;
    }
}
