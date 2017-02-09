"use strict";
var router_1 = require("@angular/router");
var login_component_1 = require("./login/login.component");
var routes = [
    {
        path: '',
        component: login_component_1.LoginComponent
    }
];
exports.AuthenticationRoutes = router_1.RouterModule.forChild(routes);
