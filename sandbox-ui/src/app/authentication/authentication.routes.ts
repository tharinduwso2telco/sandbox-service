import {RouterModule} from "@angular/router";
import {LoginComponent} from "./login/login.component";

const routes = [
    {
        path : '',
        component : LoginComponent
    }
];

export const AuthenticationRoutes = RouterModule.forChild(routes);
