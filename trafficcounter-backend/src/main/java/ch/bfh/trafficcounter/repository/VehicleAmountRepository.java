/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.VehicleAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for vehicle amount
 *
 * @author Sven Trachsel
 * @see VehicleAmount
 */
@Repository
public interface VehicleAmountRepository extends JpaRepository<VehicleAmount, Long> {
    boolean existsByIdIsGreaterThan(Long id);
}
