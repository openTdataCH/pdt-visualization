/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {TestBed} from '@angular/core/testing';

import {VehicleDataService} from './vehicle-data.service';
import {HttpClientModule} from "@angular/common/http";
import {ApiModule} from "../../api/api.module";

describe('VehicleDataServiceService', () => {
  let service: VehicleDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        ApiModule
      ],
    });
    service = TestBed.inject(VehicleDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
