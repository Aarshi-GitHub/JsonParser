import { TestBed } from '@angular/core/testing';

import { RestClientServiceService } from './rest-client-service.service';

describe('RestClientServiceService', () => {
  let service: RestClientServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RestClientServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
