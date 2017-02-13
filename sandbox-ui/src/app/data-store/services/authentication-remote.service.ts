import {Injectable, Inject} from '@angular/core';
import {Http, Headers, RequestOptions, Response} from "@angular/http";
import {AuthActionCreatorService} from "../actions/auth-action-creator.service";
import {LoginRequestParam, IUserInfo} from "../models/authentocation-models";
import {MessageService} from "../../shared/services/message.service";

@Injectable()
export class AuthenticationRemoteService {

    private headers: Headers = new Headers({
        'Content-Type': 'application/soap+xml'
    });
    private options: RequestOptions = new RequestOptions({headers: this.headers});

    constructor(private http: Http,
                private authActionCreator: AuthActionCreatorService,
                private message: MessageService,
                @Inject('AUTH_SERVER_URL') private baseUrl: string) {
    }

    private getLoginRequest(params: LoginRequestParam) {
        return '<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" x' +
            'mlns:aut="http://authentication.services.core.carbon.wso2.org"><soap:Header/><soap:Body><aut:login>' +
            '<aut:username>' + params.userName + '</aut:username>' +
            '<aut:password>' + params.password + '</aut:password>' +
            '<aut:remoteAddress>localhost</aut:remoteAddress>' +
            '</aut:login></soap:Body></soap:Envelope>';
    }

    private loginAdaptor(request: LoginRequestParam, response: string) {
        let useInfo: IUserInfo = null;

        if (!!response) {
            let stIndex = response.search('<ns:return>');
            let endIndex = response.search('</ns:return>');
            if (stIndex >= 0 && endIndex >= 0) {
                let flag = response.slice(stIndex, endIndex).split('>')[1];
                useInfo = {
                    userName: request.userName,
                    isLoggedIn: flag == 'true',
                    roles: []
                }
            }
        }
        return useInfo
    };

    doLogin(loginReq: LoginRequestParam) {
        this.http.post('/login/services/AuthenticationAdmin', this.getLoginRequest(loginReq), this.options)
            .subscribe((response: Response) => {
                    this.authActionCreator.setLoginData(this.loginAdaptor(loginReq, response.text()))
                },
                (error) => {
                    this.message.error(error);
                });
    }

    doLogout() {
    }

}
