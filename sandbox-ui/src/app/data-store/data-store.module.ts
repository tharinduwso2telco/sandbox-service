import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StoreModule} from '@ngrx/store';
import {AuthenticationReducer} from "./reducers/authentication-reducer";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {AuthenticationRemoteService} from "./services/authentication-remote.service";
import {AuthActionCreatorService} from "./actions/auth-action-creator.service";
import {AppReducer} from "./reducers/app-reducer";
import {ApplicationActionCreatorService} from "./actions/application-action-creator.service";
import {ManageActionCreatorService} from "./actions/manage-action-creator.service";
import {ManageNumberReducer} from "./reducers/manage-numbers-reducer";
import {ManageRemoteService} from "./services/manage-remote.service";

@NgModule({
    imports: [
        CommonModule,
        StoreModule.provideStore({
            AuthData: AuthenticationReducer,
            AppData : AppReducer,
            ManageNumber:ManageNumberReducer
        }),
        StoreDevtoolsModule.instrumentOnlyWithExtension({
            maxAge: 5
        })
    ],
    providers: [
        AuthActionCreatorService,
        ApplicationActionCreatorService,
        ManageActionCreatorService,
        AuthenticationRemoteService,
        ManageRemoteService
    ],
    declarations: [],
    exports: [
        StoreModule
    ]
})
export class DataStoreModule {
}
