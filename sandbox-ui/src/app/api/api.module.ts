import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ApiMainComponent} from './main/main.component';
import {ApiRoutes} from "./api.routes";

@NgModule({
    imports: [
        CommonModule,
        ApiRoutes
    ],
    declarations: [ApiMainComponent]
})
export class ApiModule {
}
