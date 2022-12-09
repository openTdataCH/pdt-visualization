import { HistoricDataDto } from './historic-data-dto';

export interface HistoricDataCollectionDto {
    resolution: string;
    measurements: Array<HistoricDataDto>;
}
