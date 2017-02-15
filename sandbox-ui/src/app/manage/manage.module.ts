import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ManageMainComponent} from './main/manage-main.component';
import {ManageRoutes} from "./manage.routes";
import { NumbersComponent } from './numbers/numbers.component';
import { AddressesComponent } from './addresses/addresses.component';
import {SharedModule} from "../shared/shared.module";
import {DDTEditCreateNumberComponent} from "./ddt-edit-create-number/ddt-edit-create-number.component";



@NgModule({
    imports: [
        CommonModule,
        ManageRoutes,
        SharedModule
    ],
    declarations: [ManageMainComponent, NumbersComponent, AddressesComponent, DDTEditCreateNumberComponent],
    entryComponents : [
        DDTEditCreateNumberComponent
    ]
})
export class ManageModule {
}
