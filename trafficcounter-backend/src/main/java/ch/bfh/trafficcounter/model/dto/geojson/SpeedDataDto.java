package ch.bfh.trafficcounter.model.dto.geojson;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents speed data of a measurement point.
 *
 * @author Manuel Riesen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpeedDataDto {

    /**
     * Average speed.
     */
    private float averageSpeed;

    /**
     * Speed display class name.
     * @see SpeedDisplayClass
     */
    private String speedDisplayClass;

}
