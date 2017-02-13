import {Component, OnInit, Inject, Input} from '@angular/core';
import {IApplicationData} from "../../../data-store/models/common-models";
import {IUserInfo} from "../../../data-store/models/authentocation-models";

@Component({
    selector: 'app-header',
    templateUrl: './app-header.component.html',
    styleUrls: ['./app-header.component.scss']
})
export class AppHeaderComponent implements OnInit {

    private isMainMenuExpand: boolean;

    @Input()
    private userInfo: IUserInfo;

    @Input()
    private applicationData: IApplicationData;

    constructor(@Inject('APP_CONSTANT') private CONST: any) {
    }

    ngOnInit() {

    }

}
