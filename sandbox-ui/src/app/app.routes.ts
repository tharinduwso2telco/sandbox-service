import {RouterModule} from "@angular/router";
import {AppGuard, LoginGuard} from "./app.guard";

const routes = [
  /*  {
        path: 'authentication',
        loadChildren: 'app/authentication/authentication.module#AuthenticationModule',
        canActivate : [LoginGuard]
    },*/
    {
        path: 'dashboard',
        loadChildren: 'app/dashboard/dashboard.module#DashboardModule',
        canActivate : [AppGuard]
    },
    {
        path : '',
        redirectTo : '/dashboard',
        pathMatch : 'full'
    },
    {
        path : '**',
        redirectTo : '/dashboard'
    }
];

export const TopLevelRoutes = RouterModule.forRoot(routes,{ useHash: true });
