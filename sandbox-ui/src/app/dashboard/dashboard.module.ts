import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from './home/home.component';
import {DashboardRoutes} from "./dashboard.routes";

@NgModule({
    imports: [
        CommonModule,
        DashboardRoutes
    ],
    declarations: [HomeComponent]
})
export class DashboardModule {
}
