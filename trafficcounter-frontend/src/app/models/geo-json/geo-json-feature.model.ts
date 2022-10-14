import {GeoJson} from "./geo-json.model";
import {GeoJsonPoint} from "./geo-json-point.model";

export interface GeoJsonFeature<T> extends GeoJson {
  readonly geometry: GeoJsonPoint;
  readonly properties: any
}

export const TYPE_FEATURE = 'Feature';
