/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {Component, OnInit} from '@angular/core';
import {MapConfigService} from "../../services/map-config/map-config.service";
import {FormControl} from "@angular/forms";
import {MapMode} from "../../models/map-mode";
import {BehaviorSubject, Observable, Subscription} from "rxjs";
import {GeoJsonPropertiesDto} from "../../../../api/models/geo-json-properties-dto";
import {VehicleDataService} from "../../../../services/vehicle-data/vehicle-data.service";
import {HistoricDataCollectionDto} from "../../../../api/models/historic-data-collection-dto";

/**
 * The menu for map options.
 */
@Component({
  selector: 'app-map-side-bar',
  templateUrl: './map-side-bar.component.html',
  styleUrls: ['./map-side-bar.component.scss']
})
export class MapSideBarComponent implements OnInit {

  isMobile$!: Observable<boolean>

  selectedPointInfo$: Observable<GeoJsonPropertiesDto | null>;

  histogramDuration$: Observable<string | null>;

  histogramData$: BehaviorSubject<HistoricDataCollectionDto | null> = new BehaviorSubject<HistoricDataCollectionDto | null>(null);

  private selectedPointSubscription: Subscription | null = null;

  private durationSubscription: Subscription | null = null;

  private historicalDataSubscription: Subscription | null = null;

  constructor(
    private readonly mapConfigService: MapConfigService,
    private readonly vehicleDataService: VehicleDataService
  ) {
    this.isMobile$ = this.mapConfigService.isMobile$;
    this.selectedPointInfo$ = this.mapConfigService.selectedPointInfo$;
    this.histogramDuration$ = this.mapConfigService.histogramDuration$;
  }

  ngOnInit(): void {

    this.selectedPointSubscription = this.mapConfigService.selectedPointInfo$.subscribe(selectedPointInfo => {
      if (selectedPointInfo !== null) {
        this.durationSubscription?.unsubscribe();
        this.durationSubscription = this.histogramDuration$.subscribe(histogramDuration => {
          if (histogramDuration !== null) {
            this.historicalDataSubscription?.unsubscribe();
            this.historicalDataSubscription = this.vehicleDataService.getHistoricalVehicleData(selectedPointInfo!.id, histogramDuration).subscribe(historicalData => {
              this.histogramData$.next(historicalData);
            });
          }
        });
      }
    });
  }


  clearSelectedPoint() {
    this.mapConfigService.showSidebar$.next(false);
    this.mapConfigService.selectedPointInfo$.next(null);
  }
}
