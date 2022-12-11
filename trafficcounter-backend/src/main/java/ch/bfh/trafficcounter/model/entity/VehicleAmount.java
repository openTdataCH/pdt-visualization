package ch.bfh.trafficcounter.model.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Represents vehicle amount.
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(indexes = @Index(columnList = "measurementPoint"))
public class VehicleAmount {

    /**
     * Technical ID of vehicle amount.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Measurement point of vehicle amount.
     */
    @ManyToOne
    private MeasurementPoint measurementPoint;

    /**
     * Measurement.
     */
    @ManyToOne
    private Measurement measurement;

    /**
     * Vehicle amount measured.
     */
    @Column(nullable = false, updatable = false)
    private Integer numberOfVehicles;
}
