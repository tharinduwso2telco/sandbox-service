"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
var core_1 = require('@angular/core');
var http_1 = require("@angular/http");
var AuthenticationRemoteService = (function () {
    function AuthenticationRemoteService(http, authActionCreator, message, baseUrl) {
        this.http = http;
        this.authActionCreator = authActionCreator;
        this.message = message;
        this.baseUrl = baseUrl;
        this.headers = new http_1.Headers({ 'Content-Type': 'text/xml' });
        this.options = new http_1.RequestOptions({ headers: this.headers });
    }
    AuthenticationRemoteService.prototype.getLoginRequest = function (params) {
        return '<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" x' +
            'mlns:aut="http://authentication.services.core.carbon.wso2.org"><soap:Header/><soap:Body><aut:login>' +
            '<aut:username>' + params.userName + '</aut:username>' +
            '<aut:password>' + params.password + '</aut:password>' +
            '<aut:remoteAddress>localhost</aut:remoteAddress>' +
            '</aut:login></soap:Body></soap:Envelope>';
    };
    AuthenticationRemoteService.prototype.doLogin = function (loginReq) {
        var _this = this;
        this.http.post(this.baseUrl['login'], this.getLoginRequest(loginReq), this.options)
            .subscribe(function (response) {
            console.log(response);
        }, function (error) {
            _this.message.error(error);
        });
    };
    AuthenticationRemoteService.prototype.doLogout = function () {
    };
    AuthenticationRemoteService = __decorate([
        core_1.Injectable(),
        __param(3, core_1.Inject('AUTH_SERVER_URL'))
    ], AuthenticationRemoteService);
    return AuthenticationRemoteService;
}());
exports.AuthenticationRemoteService = AuthenticationRemoteService;
