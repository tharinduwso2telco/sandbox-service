import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';

import {AppComponent} from './app.component';
import {TopLevelRoutes} from "./app.routes";
import {SharedModule} from "./shared/shared.module";
import {AppGuard, LoginGuard} from "./app.guard";

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        TopLevelRoutes,
        FormsModule,
        HttpModule,
        SharedModule
    ],
    providers: [
        AppGuard,
        LoginGuard
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
