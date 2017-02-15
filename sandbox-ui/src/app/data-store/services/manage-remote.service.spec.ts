/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ManageRemoteService } from './manage-remote.service';

describe('ManageRemoteService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ManageRemoteService]
    });
  });

  it('should ...', inject([ManageRemoteService], (service: ManageRemoteService) => {
    expect(service).toBeTruthy();
  }));
});
