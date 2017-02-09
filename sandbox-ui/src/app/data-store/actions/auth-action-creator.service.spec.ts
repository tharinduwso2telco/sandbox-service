/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { AuthActionCreatorService } from './auth-action-creator.service';

describe('AuthActionCreatorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuthActionCreatorService]
    });
  });

  it('should ...', inject([AuthActionCreatorService], (service: AuthActionCreatorService) => {
    expect(service).toBeTruthy();
  }));
});
