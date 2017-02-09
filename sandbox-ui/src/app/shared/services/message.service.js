"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
var MessageService = (function () {
    function MessageService(toast) {
        this.toast = toast;
        this.toastOptions = {
            title: '',
            msg: '',
            showClose: true,
            timeout: 7000,
            theme: 'material'
        };
        this.APPROVAL_MESSAGES = {
            APPLICATION_CREATION_ASSIGN_SUCCESS: 'Application creation task successfully assigned',
            SUBSCRIPTION_CREATION_ASSIGN_SUCCESS: 'Subscription creation task successfully assigned',
            APP_CREATION_APPROVE_SUCCESS: 'Application successfully approved',
            APP_CREATION_REJECT_SUCCESS: 'Application successfully rejected',
            APP_SUBSCRIPTION_APPROVE_SUCCESS: 'Application subscription successfully approved',
            APP_SUBSCRIPTION_REJECT_SUCCESS: 'Application subscription successfully rejected'
        };
    }
    MessageService.prototype.success = function (message, title) {
        this.toast.success(Object.assign({}, this.toastOptions, { title: title, msg: message }));
    };
    MessageService.prototype.error = function (message, title) {
        this.toast.error(Object.assign({}, this.toastOptions, { title: title, msg: message }));
    };
    MessageService.prototype.warning = function (message, title) {
        this.toast.warning(Object.assign({}, this.toastOptions, { title: title, msg: message }));
    };
    MessageService.prototype.info = function (message, title) {
        this.toast.info(Object.assign({}, this.toastOptions, { title: title, msg: message }));
    };
    MessageService = __decorate([
        core_1.Injectable()
    ], MessageService);
    return MessageService;
}());
exports.MessageService = MessageService;
