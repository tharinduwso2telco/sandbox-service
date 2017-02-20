import {ApiMainComponent} from "./main/main.component";
import {RouterModule} from "@angular/router";
import {ApiTypeComponent} from "./api-type/api-type.component";
import {TestApiComponent} from "./test-api/test-api.component";



const routes = [
    {
        path: '',
        component: ApiMainComponent
    },
    {
        path: ':apiType',
        component: ApiTypeComponent
    },
    {
        path: ':apiType/:serviceType',
        component: TestApiComponent
    }
];

export const ApiRoutes = RouterModule.forChild(routes);

