import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { ApiModule } from 'src/app/api/api.module';

import { VehicleSpeedService } from './vehicle-speed-service.service';

describe('VehicleSpeedService', () => {
  let service: VehicleSpeedService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        ApiModule
      ]
    });
    service = TestBed.inject(VehicleSpeedService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
