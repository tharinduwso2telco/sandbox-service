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

@NgModule({
    imports: [
        CommonModule,
        DropdownModule.forRoot(),
        ToastyModule.forRoot(),
        SlimLoadingBarModule.forRoot(),
    ],
    declarations: [
        AppHeaderComponent,
        HamburgerMenuComponent,
        UserAvatarComponent,
        MainMenuComponent,
        BreadcrumbsComponent
    ],
    exports: [
        AppHeaderComponent,
        HamburgerMenuComponent,
        UserAvatarComponent,
        DropdownModule,
        MainMenuComponent,
        BreadcrumbsComponent,
        ToastyModule,
        SlimLoadingBarModule
    ]
})
export class SharedModule {
}
