export const ROW_SELECTED: string = 'ROW_SELECTED';
export const ROW_EDIT_ACTION: string = 'ROW_EDIT_ACTION';
export const ROW_DELETE_ACTION: string = 'ROW_DELETE_ACTION';

export class RowAction {
    id: string | number;
    name: string;
    icon: string;
    class: string;
}

export class RowActionEvent {
    action: RowAction;
    rowData: any;
}