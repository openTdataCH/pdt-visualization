import { TestBed } from '@angular/core/testing';

import { MeasurementPointService } from './measurement-point.service';
import {HttpClientModule} from "@angular/common/http";
import {ApiModule} from "../../api/api.module";

describe('MeasurementPointService', () => {
  let service: MeasurementPointService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        ApiModule
      ],
    });
    service = TestBed.inject(MeasurementPointService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
