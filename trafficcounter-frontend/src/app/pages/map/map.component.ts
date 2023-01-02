/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {Component, OnInit} from '@angular/core';
import {MeasurementPointService} from "../../services/measurement-point/measurement-point.service";
import {Observable} from "rxjs";
import {GeoJsonFeatureCollectionDto} from "../../api/models/geo-json-feature-collection-dto";
import {MapConfigService} from "./services/map-config/map-config.service";
import {VehicleDataService} from "../../services/vehicle-data/vehicle-data.service";

/**
 * The page component for the map.
 */
@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

  /**
   * All measurement points as GeoJSON.
   */
  measurementPoints$: Observable<GeoJsonFeatureCollectionDto>;

  /**
   * The vehicle data as GeoJSON.
   */
  vehicleData$: Observable<GeoJsonFeatureCollectionDto>;

  constructor(
    private readonly mapConfigService: MapConfigService,
    private readonly measurementPointService: MeasurementPointService,
    private readonly vehicleDataService: VehicleDataService
  ) {
    this.measurementPoints$ = this.measurementPointService.getAllMeasurementPoints();
    this.vehicleData$ = this.vehicleDataService.getVehicleDataReactive();
  }

  get showSidebar$(): Observable<boolean> {
    return this.mapConfigService.showSidebar$;
  }

  ngOnInit(): void {
  }

}
