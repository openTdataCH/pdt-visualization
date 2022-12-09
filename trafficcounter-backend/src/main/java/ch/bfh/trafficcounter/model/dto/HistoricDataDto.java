package ch.bfh.trafficcounter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricDataDto {

    private int ordinal;

    private LocalDateTime time;

    private int avgVehicleAmount;

    private double avgVehicleSpeed;

}
