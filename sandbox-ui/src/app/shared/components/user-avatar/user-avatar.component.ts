import {Component, OnInit, Input} from '@angular/core';


@Component({
    selector: 'app-user-avatar',
    templateUrl: './user-avatar.component.html',
    styleUrls: ['./user-avatar.component.scss']
})
export class UserAvatarComponent implements OnInit {
/*
    @Input()
    private userInfo : IUserInfo;*/

    private dropDownStatus: {isOpen: boolean} = {isOpen: false};

    constructor() {
    }

    ngOnInit() {

    }

    onClick() {
        this.dropDownStatus.isOpen = !this.dropDownStatus.isOpen;
    }

    onMenuClick(type: string) {
        switch (type) {
            case 'logout' : {

            }
        }
    }


}
