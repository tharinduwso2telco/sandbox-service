import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ManageMainComponent} from './main/manage-main.component';
import {ManageRoutes} from "./manage.routes";

@NgModule({
    imports: [
        CommonModule,
        ManageRoutes
    ],
    declarations: [ManageMainComponent]
})
export class ManageModule {
}
