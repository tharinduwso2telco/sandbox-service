import {Component, OnInit, Inject} from '@angular/core';
import {Store} from '@ngrx/store';
import {IAppState} from "../../data-store/models/common-models";
import {AuthActionCreatorService} from "../../data-store/actions/auth-action-creator.service";
import {AuthenticationRemoteService} from "../../data-store/services/authentication-remote.service";
import {LoginRequestParam, IUserInfo} from "../../data-store/models/authentocation-models";
import {Router} from "@angular/router";

@Component({
    selector: 'login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

    private userName: string;
    private password: string;
    private isSubmitted: boolean;
    private loginError: string;

    constructor(private store: Store<IAppState>,
                private router:Router,
                @Inject('APP_CONSTANT') private CONST:any,
                private authService: AuthenticationRemoteService) {
    }

    ngOnInit() {
        this.store.select('AuthData').subscribe((authData:IUserInfo) => {
           if(authData && authData.isLoggedIn){
                this.router.navigate(['home']);
           }
        });
    }

    onLoginClick(loginForm) {
        let param:LoginRequestParam = new LoginRequestParam();
        param.userName = 'admin';
        param.password = 'admin';
        this.authService.doLogin(param);
        /* this.store.dispatch(
            this.authActionCreator.setLoginData({
                isLoggedIn: true,
                userName: 'sumudu',
                roles: []
            })
        );*/
        /*  this.isSubmitted = true;
         if (loginForm.valid) {
         this._authenticationService.doLogin(this.userName, this.password, (errorMsg) => {
         this.loginError = errorMsg;
         setTimeout(() => {
         this.loginError = null
         }, 5000);
         });
         }*/
    }

    onLogout() {
        /*this.store.dispatch(
            this.authActionCreator.clearLoginData()
        );*/
    }

}
