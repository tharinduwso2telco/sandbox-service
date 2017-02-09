/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { AuthenticationRemoteService } from './authentication-remote.service';

describe('AuthenticationRemoteService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuthenticationRemoteService]
    });
  });

  it('should ...', inject([AuthenticationRemoteService], (service: AuthenticationRemoteService) => {
    expect(service).toBeTruthy();
  }));
});
