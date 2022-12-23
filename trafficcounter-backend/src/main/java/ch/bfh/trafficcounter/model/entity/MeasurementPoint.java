package ch.bfh.trafficcounter.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A single measurement point with coordinates
 * can be enabled and disabled
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MeasurementPoint {

    /**
     * Official ID of measurement point.
     */
    @Id
    private String id;

    /**
     * Latitude.
     */
    @Column(nullable = false, updatable = false)
    private double latitude;

    /**
     * Longitude.
     */
    @Column(nullable = false, updatable = false)
    private double longtitude;

    /**
     * Number of lanes.
     */
    @Column(nullable = false, updatable = false)
    private int numberOfLanes;

    /**
     * Active state.
     */
    @Column(nullable = false, updatable = false)
    private boolean active;

    /**
     * Estimated speed limit.
     */
    @Column
    private Float estimatedSpeedLimit;
}
