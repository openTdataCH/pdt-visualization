import {Injectable} from '@angular/core';
import {VehicleDataControllerService} from "../../api/services/vehicle-data-controller.service";
import {Observable} from "rxjs";
import {GeoJsonFeatureCollectionDto} from "../../api/models/geo-json-feature-collection-dto";
import {HistoricDataCollectionDto} from "../../api/models/historic-data-collection-dto";

@Injectable({
  providedIn: 'root'
})
export class VehicleDataService {

  constructor(private readonly vehicleDataControllerService: VehicleDataControllerService) {
  }

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

  /**
   * Gets the historical vehicle data from the API.
   * @param id measurement point ID
   * @param duration duration
   */
  public getHistoricalVehicleData(id: string, duration: string): Observable<HistoricDataCollectionDto> {
    return this.vehicleDataControllerService.getHistoricalVehicleData(id, duration);
  }
}
