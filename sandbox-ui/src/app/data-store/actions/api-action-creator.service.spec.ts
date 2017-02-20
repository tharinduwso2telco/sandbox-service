/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ApiActionCreatorService } from './api-action-creator.service';

describe('ApiActionCreatorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ApiActionCreatorService]
    });
  });

  it('should ...', inject([ApiActionCreatorService], (service: ApiActionCreatorService) => {
    expect(service).toBeTruthy();
  }));
});
