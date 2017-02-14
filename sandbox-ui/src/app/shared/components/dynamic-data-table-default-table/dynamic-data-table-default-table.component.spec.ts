/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DynamicDataTableDefaultTableComponent } from './dynamic-data-table-default-table.component';

describe('DynamicDataTableDefaultTableComponent', () => {
  let component: DynamicDataTableDefaultTableComponent;
  let fixture: ComponentFixture<DynamicDataTableDefaultTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DynamicDataTableDefaultTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DynamicDataTableDefaultTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
