import {RouterModule} from "@angular/router";
import {AppGuard, LoginGuard} from "./app.guard";

const routes = [
    {
        path: 'login',
        loadChildren: 'app/authentication/authentication.module#AuthenticationModule',
        canActivate : [LoginGuard]
    },
    {
        path: 'home',
        loadChildren: 'app/dashboard/dashboard.module#DashboardModule',
        canActivate : [AppGuard]
    },
    {
        path : '',
        redirectTo : '/home',
        pathMatch : 'full'
    },
    {
        path : '**',
        redirectTo : '/home'
    }
];

export const TopLevelRoutes = RouterModule.forRoot(routes,{ useHash: true });
