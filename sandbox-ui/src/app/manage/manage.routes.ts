import {RouterModule} from "@angular/router";
import {ManageMainComponent} from "./main/manage-main.component";

const routes = [
    {
        path : '',
        component : ManageMainComponent
    }
];

export const ManageRoutes = RouterModule.forChild(routes);