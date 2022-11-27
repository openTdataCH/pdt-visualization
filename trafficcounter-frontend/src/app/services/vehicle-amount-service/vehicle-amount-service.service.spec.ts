import {TestBed} from '@angular/core/testing';

import {VehicleAmountServiceService} from './vehicle-amount-service.service';
import {HttpClientModule} from "@angular/common/http";
import {ApiModule} from "../../api/api.module";

describe('VehicleAmountServiceService', () => {
  let service: VehicleAmountServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        ApiModule
      ],
    });
    service = TestBed.inject(VehicleAmountServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
