import {Component, OnInit} from '@angular/core';
import {Router, NavigationEnd} from "@angular/router";
import {MessageService} from "../../services/message.service";

@Component({
    selector: 'app-breadcrumbs',
    templateUrl: './breadcrumbs.component.html',
    styleUrls: ['./breadcrumbs.component.scss']
})
export class BreadcrumbsComponent implements OnInit {

    private activeView: string[] = ['Home'];

    constructor(private _router: Router,
                private message: MessageService) {
    }

    ngOnInit() {
        this._router.events
            .filter((event: any) => event instanceof NavigationEnd)
            .subscribe((event: NavigationEnd) => {
                this.activeView = event.url.replace('/', '').split('/');
            });
    }

    onReload() {
        this.message.info('Dashboard Data Refreshed','');
    }

}
