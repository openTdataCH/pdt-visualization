package ch.bfh.trafficcounter.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SpeedData {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private MeasurementPoint measurementPoint;

    @ManyToOne
    private Measurement measurement;

    @Column(nullable = false, updatable = false)
    private Float averageSpeed;
}
