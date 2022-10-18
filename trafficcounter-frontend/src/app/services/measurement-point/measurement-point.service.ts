import {Injectable} from '@angular/core';
import {Observable, of} from "rxjs";
import {GeoJsonFeatureCollectionDto} from "../../api/models/geo-json-feature-collection-dto";
import {MeasurementPointControllerService} from "../../api/services/measurement-point-controller.service";

@Injectable({
  providedIn: 'root'
})
export class MeasurementPointService {

  constructor(private readonly measurementPointControllerService: MeasurementPointControllerService) { }

  public getAllMeasurementPoints(): Observable<GeoJsonFeatureCollectionDto> {
    return this.measurementPointControllerService.getMeasurementPointsGeoJson();
  }
}
