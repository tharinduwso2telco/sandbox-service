/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ManageActionCreatorService } from './manage-action-creator.service';

describe('ManageActionCreatorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ManageActionCreatorService]
    });
  });

  it('should ...', inject([ManageActionCreatorService], (service: ManageActionCreatorService) => {
    expect(service).toBeTruthy();
  }));
});
