package ch.bfh.trafficcounter.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Represents a measurement of a specific time.
 *
 * @author Manuel Riesen
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Measurement {

    /**
     * Technical ID of the measurement.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Time of measurement.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime time;

    /**
     * Speed data measured at the time.
     */
    @OneToMany(mappedBy = "measurement")
    private Set<SpeedData> speedData;

}
