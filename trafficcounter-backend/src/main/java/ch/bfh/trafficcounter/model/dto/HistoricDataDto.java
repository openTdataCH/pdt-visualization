package ch.bfh.trafficcounter.model.dto;

import lombok.*;

import java.time.LocalDate;
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
