import {SpeedDataDto} from "./speed-data-dto";
import {VehicleAmountDto} from "./vehicle-amount-dto";

export interface GeoJsonPropertiesDto {
  id: number;
  speedData?: SpeedDataDto;
  vehicleAmount?: VehicleAmountDto
}
