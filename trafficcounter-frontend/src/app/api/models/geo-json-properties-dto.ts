/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {SpeedDataDto} from "./speed-data-dto";
import {VehicleAmountDto} from "./vehicle-amount-dto";

export interface GeoJsonPropertiesDto {
  id: string;
  speedData?: SpeedDataDto;
  vehicleAmount?: VehicleAmountDto
}
