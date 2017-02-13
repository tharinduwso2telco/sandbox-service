import {RouterModule} from "@angular/router";
import {ManageMainComponent} from "./main/manage-main.component";
import {NumbersComponent} from "./numbers/numbers.component";
import {AddressesComponent} from "./addresses/addresses.component";

const routes = [
    {
        path : '',
        component : ManageMainComponent
    },
    {
        path : 'numbers',
        component : NumbersComponent
    },
    {
        path : 'addresses',
        component : AddressesComponent
    }
];

export const ManageRoutes = RouterModule.forChild(routes);