import {Component, Input} from '@angular/core';
import {ApplicationActionCreatorService} from "../../../data-store/actions/application-action-creator.service";

@Component({
    selector: 'app-hamburger-menu',
    template: `
    <div class="hm-menu-container" (click)="onClick()">
      <div class="hamburger-menu" >
          <div class="bar" [ngClass]="{'animate':isExpand}"></div>	
      </div>
    </div>   
  `,
    styleUrls: ['./hamburger-menu.component.scss']
})

export class HamburgerMenuComponent {

    @Input()
    private isExpand: boolean = false;

    constructor(private appActionCreator: ApplicationActionCreatorService) {
    }

    onClick() {
        this.appActionCreator.toggleMainMenu();
    }
}
