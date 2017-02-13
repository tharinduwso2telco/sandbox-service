import {ApiMainComponent} from "./main/main.component";
import {RouterModule} from "@angular/router";

const routes = [
    {
        path : '',
        component : ApiMainComponent
    }
];

export const ApiRoutes = RouterModule.forChild(routes);