package ch.bfh.trafficcounter.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricDataDto {

    private int ordinal;

    private int avgVehicleAmount;

    private int avgVehicleSpeed;

}
