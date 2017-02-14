import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ManageMainComponent} from './main/manage-main.component';
import {ManageRoutes} from "./manage.routes";
import { NumbersComponent } from './numbers/numbers.component';
import { AddressesComponent } from './addresses/addresses.component';
import {SharedModule} from "../shared/shared.module";

@NgModule({
    imports: [
        CommonModule,
        ManageRoutes,
        SharedModule
    ],
    declarations: [ManageMainComponent, NumbersComponent, AddressesComponent]
})
export class ManageModule {
}
