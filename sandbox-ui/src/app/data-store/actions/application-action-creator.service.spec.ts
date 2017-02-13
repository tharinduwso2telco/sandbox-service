/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ApplicationActionCreatorService } from './application-action-creator.service';

describe('ApplicationActionCreatorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ApplicationActionCreatorService]
    });
  });

  it('should ...', inject([ApplicationActionCreatorService], (service: ApplicationActionCreatorService) => {
    expect(service).toBeTruthy();
  }));
});
