package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.VehicleAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for vehicle amount
 * @see VehicleAmount
 *
 * @author Sven Trachsel
 */
@Repository
public interface VehicleAmountRepository extends JpaRepository<VehicleAmount, Long> {
}
