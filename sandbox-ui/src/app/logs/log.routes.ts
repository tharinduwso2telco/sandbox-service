import {RouterModule} from "@angular/router";
import {LogsMainComponent} from "./main/logs-main.component";

const routes = [
    {
        path : '',
        component : LogsMainComponent
    }
];

export const LogRoutes = RouterModule.forChild(routes);