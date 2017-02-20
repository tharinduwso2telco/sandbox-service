/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ApiRemoteService } from './api-remote-service.service';

describe('ApiRemoteService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ApiRemoteService]
    });
  });

  it('should ...', inject([ApiRemoteService], (service: ApiRemoteService) => {
    expect(service).toBeTruthy();
  }));
});
