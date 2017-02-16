/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ResponsiveTableHelperService } from './responsive-table-helper.service';

describe('ResponsiveTableHelperService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ResponsiveTableHelperService]
    });
  });

  it('should ...', inject([ResponsiveTableHelperService], (service: ResponsiveTableHelperService) => {
    expect(service).toBeTruthy();
  }));
});
