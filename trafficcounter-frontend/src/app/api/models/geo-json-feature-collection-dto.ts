import {GeoJsonFeatureDto} from './geo-json-feature-dto';

export interface GeoJsonFeatureCollectionDto {
  features: Array<GeoJsonFeatureDto>;
  type: string;
}
