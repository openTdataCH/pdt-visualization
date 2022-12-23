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
