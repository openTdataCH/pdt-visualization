package ch.bfh.trafficcounter.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class HistoricMeasurement {

    int ordinal;

    LocalDateTime time;

    double avgVehicleSpeed;

    int avgVehicleAmount;

}
