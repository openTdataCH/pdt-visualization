package ch.bfh.trafficcounter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class HistoricMeasurement {

    String measurementPointId;

    LocalDateTime time;

    double avgVehicleSpeed;

    int avgVehicleAmount;

}
