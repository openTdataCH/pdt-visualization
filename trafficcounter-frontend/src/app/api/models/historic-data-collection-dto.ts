/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import { HistoricDataDto } from './historic-data-dto';
import {MeasurementStatsType} from "./measurement-stats-type";

export interface HistoricDataCollectionDto {
    resolution: MeasurementStatsType;
    measurements: Array<HistoricDataDto>;
}
