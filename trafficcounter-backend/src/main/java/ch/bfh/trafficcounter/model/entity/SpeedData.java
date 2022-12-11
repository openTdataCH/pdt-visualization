package ch.bfh.trafficcounter.model.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Represents speed measurement data.
 *
 * @author Manuel Riesen
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(indexes = @Index(columnList = "measurementPoint"))
public class SpeedData {

    /**
     * Technical ID of speed data.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Measurement point of the measured data.
     */
    @ManyToOne
    private MeasurementPoint measurementPoint;

    /**
     * Measurement.
     */
    @ManyToOne
    private Measurement measurement;

    /**
     * Average speed measured.
     */
    @Column(nullable = false, updatable = false)
    private Float averageSpeed;
}
