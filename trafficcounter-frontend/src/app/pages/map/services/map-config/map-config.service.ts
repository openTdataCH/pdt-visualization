/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {MapMode} from "../../models/map-mode";
import {GeoJsonPropertiesDto} from "../../../../api/models/geo-json-properties-dto";

const MOBILE_THRESHOLD = 600;

/**
 * Service for map configuration.
 */
@Injectable({
  providedIn: 'root'
})
export class MapConfigService {

  /**
   * State of the menu display.
   */
  showSidebar$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  isMobile$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(window.innerWidth < MOBILE_THRESHOLD)

  /**
   * Mode of the map.
   */
  mapMode$: BehaviorSubject<MapMode> = new BehaviorSubject<MapMode>(MapMode.MeasurementPoints);

  selectedPointInfo$: BehaviorSubject<GeoJsonPropertiesDto | null> = new BehaviorSubject<GeoJsonPropertiesDto | null>(null);

  histogramDuration$: BehaviorSubject<string | null> = new BehaviorSubject<string | null>('24h');

  constructor() {
    window.onresize = () => {
      this.isMobile$.next(window.innerWidth < MOBILE_THRESHOLD);
    }
  }
}
