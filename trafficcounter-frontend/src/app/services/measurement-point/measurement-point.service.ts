/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {GeoJsonFeatureCollectionDto} from "../../api/models/geo-json-feature-collection-dto";
import {MeasurementPointControllerService} from "../../api/services/measurement-point-controller.service";

/**
 * Service for operations related to measurement points.
 */
@Injectable({
  providedIn: 'root'
})
export class MeasurementPointService {

  constructor(private readonly measurementPointControllerService: MeasurementPointControllerService) {
  }

  /**
   * Gets all measurement points from the API as GeoJSON.
   */
  public getAllMeasurementPoints(): Observable<GeoJsonFeatureCollectionDto> {
    return this.measurementPointControllerService.getMeasurementPointsGeoJson();
  }
}
