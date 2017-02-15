/* tslint:disable:no-unused-variable */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {By} from '@angular/platform-browser';
import {DebugElement} from '@angular/core';

import {ApiMainComponent} from './main.component';

describe('MainComponent', () => {
    let component: ApiMainComponent;
    let fixture: ComponentFixture<ApiMainComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [ApiMainComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(ApiMainComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
