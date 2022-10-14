import { Injectable } from '@angular/core';
import {MeasurementPointDto} from "../../models/api/measurement-point-dto.model";
import {
  GeoJsonFeatureCollection,
  TYPE_FEATURE_COLLECTION
} from "../../models/geo-json/geo-json-feature-collection.model";
import {GeoJsonFeature, TYPE_FEATURE} from "../../models/geo-json/geo-json-feature.model";
import {TYPE_POINT} from "../../models/geo-json/geo-json-point.model";

@Injectable({
  providedIn: 'root'
})
export class GeoJsonService {

  constructor() { }

  public convertToFeatureCollection(measurementPoints: Array<MeasurementPointDto>): GeoJsonFeatureCollection<MeasurementPointDto> {
    return {
      type: TYPE_FEATURE_COLLECTION,
      features: measurementPoints.map(measurementPoint => this.convertToFeature(measurementPoint))
    };
  }

  private convertToFeature(measurementPoint: MeasurementPointDto): GeoJsonFeature<MeasurementPointDto> {
    return {
      type: TYPE_FEATURE,
      geometry: {
        type: TYPE_POINT,
        coordinates: [measurementPoint.longitude, measurementPoint.latitude]
      },
      properties: measurementPoint,
    };
  }
}
