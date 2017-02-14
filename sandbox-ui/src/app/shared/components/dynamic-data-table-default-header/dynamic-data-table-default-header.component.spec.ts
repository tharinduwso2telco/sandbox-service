/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DynamicDataTableDefaultHeaderComponent } from './dynamic-data-table-default-header.component';

describe('DynamicDataTableDefaultHeaderComponent', () => {
  let component: DynamicDataTableDefaultHeaderComponent;
  let fixture: ComponentFixture<DynamicDataTableDefaultHeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DynamicDataTableDefaultHeaderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DynamicDataTableDefaultHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
