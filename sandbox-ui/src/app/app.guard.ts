import {CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot} from "@angular/router";
import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {IAppState} from "./data-store/models/common-models";
import {IUserInfo} from "./data-store/models/interfaces/userInfo";


@Injectable()
export class AppGuard implements CanActivate {

    private isLoggedIn: boolean = false;

    constructor(private _router: Router,
                private store: Store<IAppState>) {

        this.store.select('AuthData')
            .subscribe((loginData: IUserInfo) => {
                this.isLoggedIn = loginData && loginData.isLoggedIn;
            });
    }

    canActivate() {
        if (this.isLoggedIn) {
            return true;
        } else {
            this._router.navigate(['login']);
            return false;
        }
    }
}


@Injectable()
export class LoginGuard implements CanActivate {

    private isLoggedIn: boolean = false;

    constructor(private _router: Router,
                private store: Store<IAppState>) {

        this.store.select('AuthData')
            .subscribe((loginData: IUserInfo) => {
                this.isLoggedIn = loginData && loginData.isLoggedIn;
            });
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        if (this.isLoggedIn) {
            this._router.navigate(['home']);
            return false;
        } else {
            return true;
        }
    }

}


