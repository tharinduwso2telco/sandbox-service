import {RouterModule} from "@angular/router";
import {ResourceMainComponent} from "./main/resource-main.component";


const routes = [
    {
        path : '',
        component : ResourceMainComponent
    }
];

export const ResourcesRoutes = RouterModule.forChild(routes);