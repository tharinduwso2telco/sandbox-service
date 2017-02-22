import {Injectable, Inject} from '@angular/core';
import {Headers, RequestOptions, Http, Response} from "@angular/http";
import {MessageService} from "../../shared/services/message.service";
import {ApiActionCreatorService} from "../actions/api-action-creator.service";
import 'rxjs/add/operator/reduce';
import 'rxjs/add/operator/mergeMap';
import {ApiCategory, ServiceTypeCategory, Api} from "../models/common-models";


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
                this.apiActionCreator.updateApiTypes(response.apiTypes);
            })
    }

    getApiServiceTypes(apiType: ApiCategory) {
        /*   if (!!apiType) {
         this.http.get(this.sandboxPattern + `/user/${apiType.toLowerCase()}/serviceType`)
         .map((response: Response) => response.json())
         .subscribe((response) => {
         this.apiActionCreator.setApiServiceTypes(response.apiServiceCallTypes);
         },
         () => {
         this.apiActionCreator.setApiServiceTypes(null);
         })
         }*/

    }

    getApiTypesSwagger() {
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
    }

    /**
     * Overide ApiCategory to suit to the UI
     * @param unMerge
     */
    private adaptApiTypes(unMerge: ApiCategory[]) {
        this.http.get('configs/apiTypesConfig.json')
            .map((response: Response) => response.json())
            .subscribe((result) => {
                let adapted = unMerge.map((cat: ApiCategory) => {
                    if (!!result[cat.name]) {
                        return Object.assign(cat, result[cat.name])
                    } else {
                        return cat;
                    }
                });

                this.apiActionCreator.updateApiTypes(adapted);
            })
    }

    getNameFromCamelCase(camelCaseStr:string){
        let name = '';
        if(!!camelCaseStr){
            name = camelCaseStr.split(/(?=[A-Z])/).reduce((acc, curr) => {
                acc += (' ' + curr.charAt(0).toLocaleUpperCase() + curr.substr(1));
                return acc;
            }, '');

        }
        return name;
    }

    getApiServiceTypesSwagger(apiCategory: ApiCategory) {
        if (!!apiCategory) {
            let serviceTypes = [];

            apiCategory.apis.forEach((api) => {
                this.http.get(`${this.swaggerProxy + this.swaggerBase}/${api.path}`)
                    .map((response: Response) => response.json())
                    .subscribe((result) => {
                        if (!!result.models) {
                            let serviceTypeCategory: ServiceTypeCategory = {
                                name: api.description,
                                endPoints: (!!result.apis) ? result.apis.map((api: Api) => {
                                        let t: Api = api;
                                        let name = (api.operations && api.operations[0] && api.operations[0].summary && api.operations[0].summary) || '';
                                        t.name = name;
                                        t.displayName = this.getNameFromCamelCase(name);
                                        return t;
                                    }) : []
                            };
                            serviceTypes.push(serviceTypeCategory);
                            this.apiActionCreator.updateApiRequestModels(result.models);
                        }
                    });
            });

            this.apiActionCreator.setApiServiceTypes(serviceTypes);
        }
    }

}
