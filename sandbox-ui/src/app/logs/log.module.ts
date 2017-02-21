import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LogsMainComponent} from './main/logs-main.component';
import {LogRoutes} from "./log.routes";

@NgModule({
    imports: [
        CommonModule,
        LogRoutes
    ],
    declarations: [LogsMainComponent]
})
export class LogModule {
}
