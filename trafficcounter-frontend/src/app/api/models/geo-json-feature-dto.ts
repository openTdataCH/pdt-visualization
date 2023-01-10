/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {GeoJsonGeometryDto} from './geo-json-geometry-dto';
import {GeoJsonPropertiesDto} from './geo-json-properties-dto';

export interface GeoJsonFeatureDto {
  geometry: GeoJsonGeometryDto;
  properties: GeoJsonPropertiesDto;
  type: string;
}
