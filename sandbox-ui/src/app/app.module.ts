import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {TopLevelRoutes} from "./app.routes";
import {SharedModule} from "./shared/shared.module";
import {AppGuard, LoginGuard} from "./app.guard";
import {DataStoreModule} from "./data-store/data-store.module";

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        TopLevelRoutes,
        DataStoreModule,
        SharedModule
    ],
    providers: [
        AppGuard,
        LoginGuard,
        {
            provide : 'AUTH_SERVER_PROXY_PATTERN',
            useValue : '/authentication'
        },
        {
            provide : 'SANDBOX_SERVER_PROXY_PATTERN',
            useValue : '/sandbox'
        },
        {
            provide : 'APP_CONSTANT',
            useValue : {
                appName : 'Sandbox'
            }
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
