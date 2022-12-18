package ch.bfh.trafficcounter.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementStats {

    @Id
    @GeneratedValue
    private Long id;

    private String measurementPointId;

    private LocalDateTime time;

    private double avgVehicleSpeed;

    private int avgVehicleAmount;

    private MeasurementStatsType type;

    private boolean deprecated;

    public MeasurementStats(String measurementPointId, LocalDateTime time, double avgVehicleSpeed, int avgVehicleAmount, MeasurementStatsType type) {
        this.measurementPointId = measurementPointId;
        this.time = time;
        this.avgVehicleSpeed = avgVehicleSpeed;
        this.avgVehicleAmount = avgVehicleAmount;
        this.type = type;
    }
}
