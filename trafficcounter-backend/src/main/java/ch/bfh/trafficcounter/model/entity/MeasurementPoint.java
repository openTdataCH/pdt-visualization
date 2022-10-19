package ch.bfh.trafficcounter.model.entity;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A single Measurement Point with coordinates
 * can be enabled and disabled
 *
 * @Author Sven  Trachsel
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MeasurementPoint {

	@Id
	private String id;

	private double latitude;

	private double longtitude;

	private int numberOfLanes;

	private boolean active;

}
