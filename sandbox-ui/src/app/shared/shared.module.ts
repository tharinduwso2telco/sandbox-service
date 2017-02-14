import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AppHeaderComponent} from './components/app-header/app-header.component';
import {HamburgerMenuComponent} from "./components/hamburger-menu/hamburger-menu.component";
import {UserAvatarComponent} from "./components/user-avatar/user-avatar.component";
import {DropdownModule} from "ng2-bootstrap";
import {MainMenuComponent} from "./components/main-menu/main-menu.component";
import {BreadcrumbsComponent} from "./components/breadcrumbs/breadcrumbs.component";
import {ToastyModule} from "ng2-toasty";
import {SlimLoadingBarModule} from "ng2-slim-loading-bar";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {MessageService} from "./services/message.service";
import {ResponsiveTableComponent} from "./components/responsive-table/responsive-table.component";
import { DynamicDataTableComponent } from './components/dynamic-data-table/dynamic-data-table.component';
import { DynamicDataTableDefaultHeaderComponent } from './components/dynamic-data-table-default-header/dynamic-data-table-default-header.component';
import { DynamicDataTableDefaultEditorComponent } from './components/dynamic-data-table-default-editor/dynamic-data-table-default-editor.component';
import { DynamicDataTableDefaultTableComponent } from './components/dynamic-data-table-default-table/dynamic-data-table-default-table.component';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        HttpModule,
        DropdownModule.forRoot(),
        ToastyModule.forRoot(),
        SlimLoadingBarModule.forRoot(),
    ],
    declarations: [
        AppHeaderComponent,
        HamburgerMenuComponent,
        UserAvatarComponent,
        MainMenuComponent,
        BreadcrumbsComponent,
        ResponsiveTableComponent,
        DynamicDataTableComponent,
        DynamicDataTableDefaultHeaderComponent,
        DynamicDataTableDefaultEditorComponent,
        DynamicDataTableDefaultTableComponent
    ],
    providers : [
        MessageService
    ],
    exports: [
        FormsModule,
        HttpModule,
        AppHeaderComponent,
        HamburgerMenuComponent,
        UserAvatarComponent,
        DropdownModule,
        MainMenuComponent,
        BreadcrumbsComponent,
        ToastyModule,
        SlimLoadingBarModule,
        ResponsiveTableComponent,
        DynamicDataTableComponent,
        DynamicDataTableDefaultHeaderComponent,
        DynamicDataTableDefaultEditorComponent,
        DynamicDataTableDefaultTableComponent
    ]
})
export class SharedModule {
}
