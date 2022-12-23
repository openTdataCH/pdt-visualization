import { HistoricDataDto } from './historic-data-dto';
import {MeasurementStatsType} from "./measurement-stats-type";

export interface HistoricDataCollectionDto {
    resolution: MeasurementStatsType;
    measurements: Array<HistoricDataDto>;
}
