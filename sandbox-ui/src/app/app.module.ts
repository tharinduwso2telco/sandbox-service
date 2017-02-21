import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {TopLevelRoutes} from "./app.routes";
import {SharedModule} from "./shared/shared.module";
import {AppGuard, LoginGuard} from "./app.guard";
import {DataStoreModule} from "./data-store/data-store.module";
import {DynamicDataTableDefaultHeaderComponent} from "./shared/components/dynamic-data-table-default-header/dynamic-data-table-default-header.component";
import {DynamicDataTableDefaultTableComponent} from "./shared/components/dynamic-data-table-default-table/dynamic-data-table-default-table.component";
import {DynamicDataTableDefaultEditorComponent} from "./shared/components/dynamic-data-table-default-editor/dynamic-data-table-default-editor.component";

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
            provide : 'SWAGGER_BASE_URL',
            useValue : '/api-docs'
        },
        {
            provide : 'SWAGGER_PROXY_PATTERN',
            useValue : '/swagger'
        },
        {
            provide : 'APP_CONSTANT',
            useValue : {
                appName : 'Sandbox'
            }
        }
    ],
    entryComponents : [
        DynamicDataTableDefaultHeaderComponent,
        DynamicDataTableDefaultTableComponent,
        DynamicDataTableDefaultEditorComponent
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
