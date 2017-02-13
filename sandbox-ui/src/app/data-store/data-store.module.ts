import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StoreModule} from '@ngrx/store';
import {AuthenticationReducer} from "./reducers/authentication-reducer";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {AuthenticationRemoteService} from "./services/authentication-remote.service";
import {AuthActionCreatorService} from "./actions/auth-action-creator.service";
import {AppReducer} from "./reducers/app-reducer";
import {ApplicationActionCreatorService} from "./actions/application-action-creator.service";

@NgModule({
    imports: [
        CommonModule,
        StoreModule.provideStore({
            AuthData: AuthenticationReducer,
            AppData : AppReducer
        }),
        StoreDevtoolsModule.instrumentOnlyWithExtension({
            maxAge: 5
        })
    ],
    providers: [
        AuthActionCreatorService,
        ApplicationActionCreatorService,
        AuthenticationRemoteService
    ],
    declarations: [],
    exports: [
        StoreModule
    ]
})
export class DataStoreModule {
}
