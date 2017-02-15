import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';
import {Store} from "@ngrx/store";
import {IAppState, IApplicationData} from "./data-store/models/common-models";
import {IUserInfo} from "./data-store/models/interfaces/userInfo";


@Component({
    selector: 'body',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    host:     {'[class.login]':'!userInfo?.isLoggedIn'}

})
export class AppComponent implements OnInit {
    private userInfo: IUserInfo;
    private appData:IApplicationData;

    constructor(private store: Store<IAppState>) {
    }

    ngOnInit(): void {
        this.store.select('AppData')
            .subscribe((appData: IApplicationData) => {
                this.appData =appData;
            });

        this.store.select('AuthData')
            .subscribe((userInfo: IUserInfo) => {
                this.userInfo = userInfo;
            });
    }
}
