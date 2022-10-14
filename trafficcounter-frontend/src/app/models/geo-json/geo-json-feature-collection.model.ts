import {GeoJson} from "./geo-json.model";
import {GeoJsonFeature} from "./geo-json-feature.model";

export interface GeoJsonFeatureCollection<T> extends GeoJson {
  readonly features: Array<GeoJsonFeature<T>>
}

export const TYPE_FEATURE_COLLECTION = 'FeatureCollection';
