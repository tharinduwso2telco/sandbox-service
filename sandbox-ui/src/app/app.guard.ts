import {CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot} from "@angular/router";
import {Injectable} from "@angular/core";

@Injectable()
export class AppGuard implements CanActivate {

    constructor(private _router: Router) {
    }

    canActivate() {
      /*  if (this._authenticationService.isLoggedIn()) {
            return true;
        } else {*/
            this._router.navigate(['login']);
            return false;
      //  }
    }
}


@Injectable()
export class LoginGuard implements CanActivate {

    constructor(private _router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return true;
    }

}


