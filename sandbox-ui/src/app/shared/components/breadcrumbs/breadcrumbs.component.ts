import {Component, OnInit} from '@angular/core';
import {Router, NavigationEnd} from "@angular/router";
import {MessageService} from "../../services/message.service";
import {ApplicationActionCreatorService} from "../../../data-store/actions/application-action-creator.service";

@Component({
    selector: 'app-breadcrumbs',
    templateUrl: './breadcrumbs.component.html',
    styleUrls: ['./breadcrumbs.component.scss']
})
export class BreadcrumbsComponent implements OnInit {

    private activeView: string[] = ['Home'];

    constructor(private _router: Router,
                private message: MessageService,
                private appActionCreator: ApplicationActionCreatorService) {
    }

    ngOnInit() {
        this._router.events
            .filter((event: any) => event instanceof NavigationEnd)
            .subscribe((event: NavigationEnd) => {
                this.activeView = event.url.replace('/', '').split('/');
            });
    }

    onReload() {
        this.appActionCreator.realodData();
        this.message.info('Dashboard Data Refreshed', '');
    }

    onLinkClick(path: string) {
        let index = this.activeView.indexOf(path);
        let myPath: string = '';
        if (index >= 0) {
            this.activeView.slice(0, index + 1).forEach((subPath) => {
                myPath += ('/' + subPath);
            });
        }
        this._router.navigate([myPath]);
    }

}
