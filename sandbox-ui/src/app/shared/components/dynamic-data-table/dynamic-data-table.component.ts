import {
    Component, OnInit, ViewChild, ViewContainerRef, Input, ReflectiveInjector, Injector,
    ResolvedReflectiveProvider, ComponentFactoryResolver
} from '@angular/core';
import {DynamicComponentData} from "../../../data-store/models/common-models";
import {DynamicDataTableDefaultHeaderComponent} from "../dynamic-data-table-default-header/dynamic-data-table-default-header.component";
import {DynamicDataTableDefaultTableComponent} from "../dynamic-data-table-default-table/dynamic-data-table-default-table.component";
import {DynamicDataTableDefaultEditorComponent} from "../dynamic-data-table-default-editor/dynamic-data-table-default-editor.component";

@Component({
    selector: 'dynamic-data-table',
    templateUrl: './dynamic-data-table.component.html',
    styleUrls: ['./dynamic-data-table.component.scss'],
    entryComponents : [
        DynamicDataTableDefaultHeaderComponent,
        DynamicDataTableDefaultTableComponent,
        DynamicDataTableDefaultEditorComponent
    ]
})
export class DynamicDataTableComponent implements OnInit {


    @ViewChild('dynamicHeaderContainer', {read: ViewContainerRef}) dynamicHeaderContainer: ViewContainerRef;

    @ViewChild('dynamicTableContainer', {read: ViewContainerRef}) dynamicTableContainer: ViewContainerRef;

    @ViewChild('dynamicEditContainer', {read: ViewContainerRef}) dynamicEditContainer: ViewContainerRef;

    constructor(private resolver:ComponentFactoryResolver) {
    }

    private tmpDynamicHeader:any;

    private tmpDynamicTable:any;

    private tmpDynamicEditor:any;

    private getInputProvider(data) {
        return Object.keys(data.inputData).map((fieldName) => ({
            provide: fieldName,
            useValue: data.inputData[fieldName]
        }));
    }



    private getComponent(data:DynamicComponentData,viewContainerRef:ViewContainerRef){
        let inputProvider = this.getInputProvider(data);
        let resolvedInputs = ReflectiveInjector.resolve(inputProvider);
        let injector = ReflectiveInjector.fromResolvedProviders(resolvedInputs,viewContainerRef.parentInjector);
        let factory = this.resolver.resolveComponentFactory(data.component);
        let component = factory.create(injector);
        return component;
    }

    @Input() set headerData(data: DynamicComponentData) {
        if (!!data) {
            let component = this.getComponent(data,this.dynamicHeaderContainer);
            this.dynamicHeaderContainer.insert(component.hostView);

            if(!!this.tmpDynamicHeader){
                this.tmpDynamicHeader.destroy();
            }
            this.tmpDynamicHeader = component;
        }
    }

    @Input() set tableData(data: DynamicComponentData) {
        if (!!data) {
            let component = this.getComponent(data,this.dynamicTableContainer);
            this.dynamicTableContainer.insert(component.hostView);

            if(!!this.tmpDynamicTable){
                this.tmpDynamicTable.destroy();
            }
            this.tmpDynamicTable = component;
        }
    }

    @Input() set editorData(data: DynamicComponentData) {
        if (!!data) {
            let component = this.getComponent(data,this.dynamicEditContainer);
            this.dynamicEditContainer.insert(component.hostView);

            if(!!this.tmpDynamicEditor){
                this.tmpDynamicEditor.destroy();
            }
            this.tmpDynamicEditor = component;
        }
    }

    ngOnInit() {
    }

}
