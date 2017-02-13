import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
    selector: 'manage-main',
    templateUrl: './manage-main.component.html',
    styleUrls: ['./manage-main.component.scss']
})
export class ManageMainComponent implements OnInit {

    constructor(private router: Router) {
    }

    ngOnInit() {
    }

    onIconClick(type:string) {
        if (type == 'number') {
            this.router.navigate(['/manage/number'])
        }else if (type == 'address') {
            this.router.navigate(['/manage/address'])
        }
    }

}
