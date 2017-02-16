import {Injectable, Inject} from '@angular/core';
import {ManageActionCreatorService} from "../actions/manage-action-creator.service";
import {Headers, RequestOptions, Http, Response} from "@angular/http";
import {Store} from "@ngrx/store";
import {IAppState} from "../models/common-models";
import {IUserInfo} from "../models/interfaces/userInfo";
import {MessageService} from "../../shared/services/message.service";
import {UserNumber} from "../models/manage-numbers-model";

@Injectable()
export class ManageRemoteService {

    private headers: Headers = new Headers(
        {
            'Content-Type': 'application/json',
            'sandbox': 'admin'
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
                        let userNumber: UserNumber = {
                            id : responseObk.id,
                            number: responseObk.number,
                            numberBalance: responseObk.balance,
                            description: responseObk.description,
                            imsi: responseObk.IMSI,
                            mnc: responseObk.MNC,
                            reservedAmount: responseObk.reserved_amount,
                            status: responseObk.status,
                            mcc :responseObk.MCC
                        };
                        return userNumber;
                    });
                } else {
                    return new Array();
                }
            })
            .subscribe(
                (numbers: UserNumber[]) => {
                    let sorted = numbers.sort((n1:UserNumber,n2:UserNumber)=>{
                       return n2.id - n1.id;
                    });
                    this.manageActionCreator.updateUserNumbers(sorted);
                },
                (error) => {
                    this.message.error(error);
                }
            )
    }

    addUserNumber(userNumber: UserNumber): void {
        delete userNumber.id;

        this.http.post(this.sandboxPattern + '/user/managenumber', userNumber, this.options)
            .map((response: Response) => response.text())
            .subscribe(
                (result) => {
                    this.getUserNumbers();
                    this.message.success(this.message.MANAGE_MESSAGES.NUMBER_ADD_SUCCESS)
                    this.manageActionCreator.closeEditorPanel();
                },
                (error) => {
                    this.message.error(error)
                }
            )
    }

    deleteNumber(userNumber:UserNumber){
        //TODO No REST API AVAILABLE
        this.message.success(this.message.MANAGE_MESSAGES.NUMBER_DELETE_SUCCESS)
    }

}
