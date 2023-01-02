/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {TestBed} from '@angular/core/testing';

import {MapConfigService} from './map-config.service';

describe('MapConfigService', () => {
  let service: MapConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MapConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
