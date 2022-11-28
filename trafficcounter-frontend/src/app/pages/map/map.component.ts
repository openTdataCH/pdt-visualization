import {Component, OnInit} from '@angular/core';
import {MeasurementPointService} from "../../services/measurement-point/measurement-point.service";
import {Observable} from "rxjs";
import {GeoJsonFeatureCollectionDto} from "../../api/models/geo-json-feature-collection-dto";
import {MapConfigService} from "./services/map-config/map-config.service";
import {VehicleAmountServiceService} from "../../services/vehicle-amount-service/vehicle-amount-service.service";
import { VehicleSpeedService } from 'src/app/services/vehicle-speed-service/vehicle-speed-service.service';

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
   * The amount of vehicles as GeoJSON.
   */
  vehicleAmount$: Observable<GeoJsonFeatureCollectionDto>;

  /**
   * The speed of vehicles as GeoJSON.
   */
  vehicleSpeed$: Observable<GeoJsonFeatureCollectionDto>;

  get showMenu(): Observable<boolean> {
    return this.mapConfigService.showMenu$;
  }

  constructor(
    private readonly mapConfigService: MapConfigService,
    private readonly measurementPointService: MeasurementPointService,
    private readonly vehicleAmountService: VehicleAmountServiceService,
    private readonly vehicleSpeedService: VehicleSpeedService
  ) {
    this.measurementPoints$ = this.measurementPointService.getAllMeasurementPoints();
    this.vehicleAmount$ = this.vehicleAmountService.getVehicleAmountReactive();
    this.vehicleSpeed$ = this.vehicleSpeedService.getSpeedDataReactive();
  }

  ngOnInit(): void {
  }

}
