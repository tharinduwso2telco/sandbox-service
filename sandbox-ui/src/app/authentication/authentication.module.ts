import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponent} from './login/login.component';
import {AuthenticationRoutes} from "./authentication.routes";
import {FormsModule} from "@angular/forms";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        AuthenticationRoutes
    ],
    declarations: [LoginComponent]
})
export class AuthenticationModule {
}
