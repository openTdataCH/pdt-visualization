import { TestBed } from '@angular/core/testing';

import { VehicleAmountServiceService } from './vehicle-amount-service.service';

describe('VehicleAmountServiceService', () => {
  let service: VehicleAmountServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VehicleAmountServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
