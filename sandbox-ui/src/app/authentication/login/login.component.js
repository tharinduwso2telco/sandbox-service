"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
var authentocation_models_1 = require("../../data-store/models/authentocation-models");
var LoginComponent = (function () {
    function LoginComponent(store, authService) {
        this.store = store;
        this.authService = authService;
    }
    LoginComponent.prototype.ngOnInit = function () {
        this.store.select('AuthData').subscribe(function (authData) {
            console.log(authData);
        });
        this.store.subscribe(function (appState) {
            console.log(appState);
        });
    };
    LoginComponent.prototype.onLoginClick = function (loginForm) {
        var param = new authentocation_models_1.LoginRequestParam();
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
    };
    LoginComponent.prototype.onLogout = function () {
        /*this.store.dispatch(
            this.authActionCreator.clearLoginData()
        );*/
    };
    LoginComponent = __decorate([
        core_1.Component({
            selector: 'login',
            templateUrl: './login.component.html',
            styleUrls: ['./login.component.scss']
        })
    ], LoginComponent);
    return LoginComponent;
}());
exports.LoginComponent = LoginComponent;
