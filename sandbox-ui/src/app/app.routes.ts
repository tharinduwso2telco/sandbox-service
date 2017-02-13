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
        path: 'api',
        loadChildren: 'app/api/api.module#ApiModule',
        canActivate : [AppGuard]
    },
    {
        path: 'manage',
        loadChildren: 'app/manage/manage.module#ManageModule',
        canActivate : [AppGuard]
    },
    {
        path: 'log',
        loadChildren: 'app/log/log.module#LogModule',
        canActivate : [AppGuard]
    },
    {
        path: 'resources',
        loadChildren: 'app/resources/resources.module#ResourcesModule',
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
