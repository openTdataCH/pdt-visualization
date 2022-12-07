package ch.bfh.trafficcounter.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class HistoricMeasurement {

    int ordinal;

    double avgVehicleSpeed;

    int avgVehicleAmount;

}
