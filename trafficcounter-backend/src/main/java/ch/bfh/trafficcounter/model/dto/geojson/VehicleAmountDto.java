package ch.bfh.trafficcounter.model.dto.geojson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents amounts of vehicles of a measurement point.
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleAmountDto {

	/**
	 * Amount of vehicles
	 */
	private float numberOfVehicles;

}

