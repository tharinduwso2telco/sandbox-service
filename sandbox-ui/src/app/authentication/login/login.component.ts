import {Component, OnInit} from '@angular/core';

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

    constructor() {
    }

    ngOnInit() {
    }

    onLoginClick(loginForm) {
        this.isSubmitted = true;
        if (loginForm.valid) {
          /*  this._authenticationService.doLogin(this.userName, this.password, (errorMsg) => {
                this.loginError = errorMsg;
                setTimeout(() => {
                    this.loginError = null
                }, 5000);
            });*/
        }

    }

}
