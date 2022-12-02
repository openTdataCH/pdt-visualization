import {TestBed} from '@angular/core/testing';

import {VehicleAmountService} from './vehicle-amount.service';
import {HttpClientModule} from "@angular/common/http";
import {ApiModule} from "../../api/api.module";

describe('VehicleAmountServiceService', () => {
  let service: VehicleAmountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        ApiModule
      ],
    });
    service = TestBed.inject(VehicleAmountService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
