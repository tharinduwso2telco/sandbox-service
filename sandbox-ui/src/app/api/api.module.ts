import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ApiMainComponent} from './main/main.component';
import {ApiRoutes} from "./api.routes";
import {ApiHelperService} from "./api-helper.service";
import { ServiceTypeComponent } from './service-type/service-type.component';
import { ApiTypeComponent } from './api-type/api-type.component';
import { TestApiComponent } from './test-api/test-api.component';
import { ConfigureApiComponent } from './configure-api/configure-api.component';
import {SharedModule} from "../shared/shared.module";

@NgModule({
    imports: [
        CommonModule,
        ApiRoutes,
        SharedModule
    ],
    declarations: [
        ApiMainComponent, ServiceTypeComponent, ApiTypeComponent, TestApiComponent, ConfigureApiComponent
    ],
    providers : [
        ApiHelperService
    ]
})
export class ApiModule {
}
