import { Injectable } from '@angular/core';
import {VehicleDataControllerService} from "../../api/services/vehicle-data-controller.service";
import {Observable} from "rxjs";
import {GeoJsonFeatureCollectionDto} from "../../api/models/geo-json-feature-collection-dto";

@Injectable({
  providedIn: 'root'
})
export class VehicleDataService {

  constructor(private readonly vehicleDataControllerService: VehicleDataControllerService) { }

  /**
   * Gets the amount of vehicles from the API as GeoJSON.
   */
  public getVehicleData(): Observable<GeoJsonFeatureCollectionDto> {
    return this.vehicleDataControllerService.getCurrentVehicleData();
  }

  /**
   * Gets the amount of vehicles from the API as GeoJSON in a reactive way.
   */
  public getVehicleDataReactive(): Observable<GeoJsonFeatureCollectionDto> {
    return this.vehicleDataControllerService.getCurrentVehicleDataReactive();
  }
}
