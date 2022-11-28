import { Injectable } from '@angular/core';
import {VehicleAmountControllerService} from "../../api/services/vehicle-amount-controller.service";
import {Observable} from "rxjs";
import {GeoJsonFeatureCollectionDto} from "../../api/models/geo-json-feature-collection-dto";


/**
 * Service for operations related to the amount of vehicles.
 */
@Injectable({
  providedIn: 'root'
})
export class VehicleAmountServiceService {

  constructor(private readonly vehicleAmountControllerService: VehicleAmountControllerService) { }

  /**
   * Gets the amount of vehicles from the API as GeoJSON.
   */
  public getVehicleAmount(): Observable<GeoJsonFeatureCollectionDto> {
    return this.vehicleAmountControllerService.getCurrentAmountOfVehicles();
  }

  /**
   * Gets the amount of vehicles from the API as GeoJSON in a reactive way.
   */
  public getVehicleAmountReactive(): Observable<GeoJsonFeatureCollectionDto> {
    return this.vehicleAmountControllerService.getCurrentAmountOfVehiclesReactive();
  }


}
