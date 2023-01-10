/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.SpeedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for speed data.
 *
 * @author Manuel Riesen
 * @see SpeedData
 */
@Repository
public interface SpeedDataRepository extends JpaRepository<SpeedData, Long> {
    boolean existsByIdIsGreaterThan(Long id);
}
