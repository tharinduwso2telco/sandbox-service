import {Injectable} from '@angular/core';
import {ToastyService, ToastOptions, ToastyConfig, toastyServiceFactory} from "ng2-toasty";

@Injectable()
export class MessageService {

    constructor(private toast: ToastyService) {
    }

    private toastOptions: ToastOptions = {
        title: '',
        msg: '',
        showClose: true,
        timeout: 7000,
        theme: 'material'
    };

    public MANAGE_MESSAGES= {
        NUMBER_ADD_SUCCESS : 'Number successfully added',
        NUMBER_DELETE_SUCCESS : 'Number successfully deleted'
    };


    success(message: string, title?: string) {
        this.toast.success(Object.assign({}, this.toastOptions, {title, msg: message}));
    }

    error(message: string, title?: string) {
        this.toast.error(Object.assign({}, this.toastOptions, {title, msg: message}));
    }

    warning(message: string, title?: string) {
        this.toast.warning(Object.assign({}, this.toastOptions, {title, msg: message}));
    }

    info(message: string, title?: string) {
        this.toast.info(Object.assign({}, this.toastOptions, {title, msg: message}));
    }

}
