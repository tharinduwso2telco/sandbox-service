import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ResourceMainComponent} from './main/resource-main.component';
import {ResourcesRoutes} from "./resources.routes";

@NgModule({
    imports: [
        CommonModule,
        ResourcesRoutes
    ],
    declarations: [ResourceMainComponent]
})
export class ResourcesModule {
}
