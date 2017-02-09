import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StoreModule} from '@ngrx/store';
import {AuthenticationReducer} from "./reducers/authentication-reducer";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {AuthenticationRemoteService} from "./services/authentication-remote.service";
import {AuthActionCreatorService} from "./actions/auth-action-creator.service";

@NgModule({
    imports: [
        CommonModule,
        StoreModule.provideStore({
            AuthData: AuthenticationReducer
        }),
        StoreDevtoolsModule.instrumentOnlyWithExtension({
            maxAge: 5
        })
    ],
    providers: [
        AuthActionCreatorService,
        AuthenticationRemoteService
    ],
    declarations: [],
    exports: [
        StoreModule
    ]
})
export class DataStoreModule {
}
