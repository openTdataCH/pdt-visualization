import { GeoJsonGeometryDto } from './geo-json-geometry-dto';
import { GeoJsonPropertiesDto } from './geo-json-properties-dto';

export interface GeoJsonFeatureDto {
    geometry: GeoJsonGeometryDto;
    properties: GeoJsonPropertiesDto;
    type: string;
}
