import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable()
export class AppCommonService {

    public MenuToggleProvider: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

    constructor() {
    }

    updateMenuToggleStream(flag:boolean){
        this.MenuToggleProvider.next(flag);
    }

}
