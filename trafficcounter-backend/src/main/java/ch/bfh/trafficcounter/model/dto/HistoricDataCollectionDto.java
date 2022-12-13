package ch.bfh.trafficcounter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricDataCollectionDto {

    private String resolution;

    private HistoricDataDto[] measurements;

}
