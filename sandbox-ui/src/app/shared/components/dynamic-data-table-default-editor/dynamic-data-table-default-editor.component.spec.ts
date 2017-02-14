/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DynamicDataTableDefaultEditorComponent } from './dynamic-data-table-default-editor.component';

describe('DynamicDataTableDefaultEditorComponent', () => {
  let component: DynamicDataTableDefaultEditorComponent;
  let fixture: ComponentFixture<DynamicDataTableDefaultEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DynamicDataTableDefaultEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DynamicDataTableDefaultEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
