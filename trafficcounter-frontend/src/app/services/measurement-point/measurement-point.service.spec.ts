import { TestBed } from '@angular/core/testing';

import { MeasurementPointService } from './measurement-point.service';

describe('MeasurementPointService', () => {
  let service: MeasurementPointService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MeasurementPointService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
