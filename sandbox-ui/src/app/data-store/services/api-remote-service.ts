import {Injectable, Inject} from '@angular/core';
import {Headers, RequestOptions, Http, Response} from "@angular/http";
import {MessageService} from "../../shared/services/message.service";
import {ApiActionCreatorService} from "../actions/api-action-creator.service";
import 'rxjs/add/operator/reduce';
import 'rxjs/add/operator/mergeMap';
import {ApiCategory, ServiceTypeCategory, Api, ApiServiceDefinition} from "../models/common-models";
import {Observable} from "rxjs";


@Injectable()
export class ApiRemoteService {

    constructor(private http: Http,
                private apiActionCreator: ApiActionCreatorService,
                private message: MessageService,
                @Inject('SANDBOX_SERVER_PROXY_PATTERN') private sandboxPattern: string,
                @Inject('SWAGGER_PROXY_PATTERN') private swaggerProxy: string,
                @Inject('SWAGGER_BASE_URL') private swaggerBase: string) {
    }

    private headers: Headers = new Headers(
        {
            'Content-Type': 'application/json',
            'sandbox': 'admin'
        }
    );

    private options: RequestOptions = new RequestOptions({headers: this.headers});


    getApiTypes() {
        this.http.get(this.sandboxPattern + '/user/apiType', this.options)
            .map((response: Response) => response.json())
            .subscribe((response) => {
                this.adaptApiTypes(response.apiTypes);
            })
    }

    getApiServiceTypes(apiType: ApiCategory) {
        if (!!apiType) {
            this.http.get('configs/apiServicesConfig.json')
                .map((response: Response) => {
                    let obj = response.json();
                    return obj[apiType.name] || {};
                })
                .subscribe((response) => {
                        this.apiActionCreator.setApiServiceTypes(response);
                    },
                    () => {
                        this.apiActionCreator.setApiServiceTypes(null);
                    })

        }

    }

    /*getApiTypesSwagger() {
     this.http.get(`${this.swaggerProxy + this.swaggerBase}`)
     .map((response: Response) => response.json())
     .flatMap((jsonResponse) => jsonResponse.apis)
     .reduce((acc: ApiCategory[], apiObj: any) => {
     let apiName = apiObj.path && (apiObj.path.split('/')[0] || apiObj.path.split('/')[1]);

     let foundIndex = acc.findIndex((cat: ApiCategory) => cat.name == apiName)
     if (foundIndex >= 0) {
     (<ApiCategory>acc[foundIndex]).apis.push(apiObj);
     } else {
     acc.push({
     name: apiName,
     apis: [apiObj]
     });
     }
     return acc;
     }, [])
     .subscribe((response) => {
     this.adaptApiTypes(response);
     })
     }*/

    /**
     * Overide ApiCategory to suit to the UI
     * @param unMerge
     */
    private adaptApiTypes(unMerge: string[]) {
        this.http.get('configs/apiTypesConfig.json')
            .map((response: Response) => response.json())
            .subscribe((result) => {
                let adapted = unMerge.map((apiType: string) => {
                    let apiTypeName = apiType.toLocaleLowerCase();

                    let apiCat: ApiCategory = {
                        name: apiTypeName,
                    };

                    if (!!result[apiTypeName]) {
                        apiCat = Object.assign(apiCat, result[apiTypeName])
                    }
                    return apiCat
                });

                this.apiActionCreator.updateApiTypes(adapted);
            })
    }

    getNameFromCamelCase(camelCaseStr: string) {
        let name = '';
        if (!!camelCaseStr) {
            name = camelCaseStr.split(/(?=[A-Z])/).reduce((acc, curr) => {
                acc += (' ' + curr.charAt(0).toLocaleUpperCase() + curr.substr(1));
                return acc;
            }, '');

        }
        return name;
    }

    getApiServiceTypesSwagger(apiCategory: ApiCategory) {
        if (!!apiCategory) {
            let resultCol: any[] = [];

            apiCategory.swaggerDefinitions.forEach((api: string) => {
                resultCol.push(this.http.get(`${this.swaggerProxy + this.swaggerBase}/${api}`)
                    .map((response: Response) => response.json()));
            });

            Observable.forkJoin(resultCol)
                .subscribe((result) => {
                    let apiServiceDef: ApiServiceDefinition = {
                        apiType: apiCategory.name,
                        apiDefinitions: []
                    };

                    result.forEach((singularResult: any) => {
                        if (!!singularResult.apis) {
                            apiServiceDef.apiDefinitions = apiServiceDef.apiDefinitions.concat(...singularResult.apis.map((api: Api) => {
                                let t: Api = api;
                                let name = (api.operations && api.operations[0] && api.operations[0].summary && api.operations[0].summary) || '';
                                t.name = name;
                                t.displayName = this.getNameFromCamelCase(name);
                                return t;
                            }));
                        }

                        if (!!singularResult.models) {
                            this.apiActionCreator.updateApiRequestModels(singularResult.models);
                        }
                    });
                    this.apiActionCreator.setApiServiceDefinitions(apiServiceDef);
                });
        }
    }

}
