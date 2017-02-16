import {Injectable} from '@angular/core';

@Injectable()
export class ResponsiveTableHelperService {

    private _selectedRow: any;

    constructor() {
    }

    set selectedRow(value: any) {
        this._selectedRow = value;
    }

    get selectedRow(): any {
        return this._selectedRow;
    }
}
