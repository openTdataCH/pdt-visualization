package ch.bfh.trafficcounter.model.dto.geojson;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpeedDataDto {

    private String measurementPointId;

    private float speed;

}
