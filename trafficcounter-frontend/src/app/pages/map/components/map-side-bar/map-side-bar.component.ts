import {Component, OnInit} from '@angular/core';
import {MapConfigService} from "../../services/map-config/map-config.service";
import {FormControl} from "@angular/forms";
import {MapMode} from "../../models/map-mode";
import {BehaviorSubject, Observable} from "rxjs";
import {GeoJsonPropertiesDto} from "../../../../api/models/geo-json-properties-dto";
import {HistogramType} from "../histogram/histogram-type";
import {HistoricDataCollectionDto} from "../../../../api/models/historic-data-collection-dto";
import {VehicleDataService} from "../../../../services/vehicle-data/vehicle-data.service";

/**
 * The menu for map options.
 */
@Component({
  selector: 'app-map-side-bar',
  templateUrl: './map-side-bar.component.html',
  styleUrls: ['./map-side-bar.component.scss']
})
export class MapSideBarComponent implements OnInit {

  modeControl = new FormControl(MapMode.MeasurementPoints);

  selectedPointInfo$: Observable<GeoJsonPropertiesDto | null>;

  //histogramType$: Observable<HistogramType | null>;

  histogramDuration$: Observable<string | null>;

  histogramData$: BehaviorSubject<HistoricDataCollectionDto | null> = new BehaviorSubject<HistoricDataCollectionDto | null>(null);

  constructor(
    private readonly mapConfigService: MapConfigService,
    private readonly vehicleDataService: VehicleDataService
  ) {
    this.selectedPointInfo$ = this.mapConfigService.selectedPointInfo$;
    //this.histogramType$ = this.mapConfigService.histogramType$;
    this.histogramDuration$ = this.mapConfigService.histogramDuration$;
  }

  ngOnInit(): void {
    this.mapConfigService.selectedPointInfo$.subscribe(selectedPointInfo => {
      if (selectedPointInfo !== null) {
        this.histogramDuration$.subscribe(histogramDuration => {
          if (histogramDuration !== null) {
            this.vehicleDataService.getHistoricalVehicleData(selectedPointInfo!.id, histogramDuration).subscribe(historicalData => {
              this.histogramData$.next(historicalData);
            });
          }
        });
      }
    });
  }

  updateMode(): void {
    this.mapConfigService.mapMode$.next(this.modeControl.value ?? MapMode.MeasurementPoints);
  }

}
