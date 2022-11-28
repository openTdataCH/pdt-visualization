import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GeoJsonFeatureCollectionDto } from 'src/app/api/models/geo-json-feature-collection-dto';
import { SpeedDataControllerService } from 'src/app/api/services/speed-data-controller.service';

/**
 * Service for operations related to the speed of vehicles.
 */
@Injectable({
  providedIn: 'root'
})
export class VehicleSpeedService {

  constructor(private readonly vehicleSpeedControllerService: SpeedDataControllerService) { }

  /**
   * @returns speed of vehicles read from the API as GeoJSON.
   */
  public getSpeedData(): Observable<GeoJsonFeatureCollectionDto> {
    return this.vehicleSpeedControllerService.getCurrentSpeedData();
  }

  /**
   * @returns speed of vehicles read from the API as GeoJSON in a reactive way.
   */
  public getSpeedDataReactive(): Observable<GeoJsonFeatureCollectionDto> {
    return this.vehicleSpeedControllerService.getCurrentSpeedDataReactive();
  }
}
