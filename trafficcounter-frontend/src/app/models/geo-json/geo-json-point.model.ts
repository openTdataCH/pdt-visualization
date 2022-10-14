import {GeoJson} from "./geo-json.model";

export interface GeoJsonPoint extends GeoJson {
  readonly coordinates: Array<number>;
}

export const TYPE_POINT = 'Point';
