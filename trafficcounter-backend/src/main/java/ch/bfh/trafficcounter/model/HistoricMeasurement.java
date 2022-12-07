package ch.bfh.trafficcounter.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoricMeasurement {

    int ordinal;

    int avgVehicleSpeed;

    int avgVehicleAmount;

}
