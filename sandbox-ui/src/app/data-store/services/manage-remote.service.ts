import {Injectable, Inject} from '@angular/core';
import {ManageActionCreatorService} from "../actions/manage-action-creator.service";
import {Headers, RequestOptions, Http, Response} from "@angular/http";
import {Store} from "@ngrx/store";
import {IAppState, IUserNumber} from "../models/common-models";
import {IUserInfo} from "../models/interfaces/userInfo";
import {MessageService} from "../../shared/services/message.service";

@Injectable()
export class ManageRemoteService {

    private headers: Headers = new Headers(
        {
            'Content-Type': 'application/json',
            'sandbox' : 'admin'
        }
    );

    private options: RequestOptions = new RequestOptions({headers: this.headers});

    private userInfo: IUserInfo;

    constructor(private manageActionCreator: ManageActionCreatorService,
                private http: Http,
                private store: Store<IAppState>,
                private message: MessageService,
                @Inject('SANDBOX_SERVER_PROXY_PATTERN') private sandboxPattern: string) {

        this.store.select('AuthData')
            .subscribe((userInfo: IUserInfo) => {
                this.userInfo = userInfo;
            });
    }

    getUserNumbers() {
        if (!this.userInfo) {
            return;
        }

        this.http.get(this.sandboxPattern + '/user/' + this.userInfo.userName + '/numberDetails', this.options)
            .map((response: Response) => {
                let tmp = response.json();
                if (!!tmp) {
                    return tmp.map((responseObk) => {
                        let userNumber: IUserNumber = {
                            number: responseObk.number,
                            balance: responseObk.balance,
                            description: responseObk.description,
                            imsi: responseObk.IMSI,
                            mnc: responseObk.MNC,
                            reserved_amount: responseObk.reserved_amount,
                            status: responseObk.status
                        };
                        return userNumber;
                    });
                } else {
                    return [];
                }
            })
            .subscribe(
                (numbers: IUserNumber[]) => {
                    this.manageActionCreator.updateUserNumbers(numbers);
                },
                (error) => {
                    this.message.error(error);
                }
            )
    }

}
