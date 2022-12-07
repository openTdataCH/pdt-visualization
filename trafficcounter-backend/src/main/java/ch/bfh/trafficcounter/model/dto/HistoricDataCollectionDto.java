package ch.bfh.trafficcounter.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricDataCollectionDto {

    private String resolution;

    private HistoricDataDto[] measurements;

}
