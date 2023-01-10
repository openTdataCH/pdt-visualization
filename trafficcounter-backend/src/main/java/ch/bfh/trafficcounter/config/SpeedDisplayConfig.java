/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.config;

import ch.bfh.trafficcounter.model.dto.geojson.SpeedDisplayClass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for speed display.
 *
 * @author Manuel Riesen
 */
@Configuration
@ConfigurationProperties(prefix = "trafficcounter.speed-display")
@Setter
@Getter
public class SpeedDisplayConfig {

    private SpeedDisplayThresholds thresholds;

    /**
     * Gets the speed display class from the average speed.
     *
     * @param speedPercentage speed percentage
     * @return speed display class name
     */
    public String getSpeedDisplayClass(final float speedPercentage) {
        if (speedPercentage >= thresholds.getHigh()) {
            return SpeedDisplayClass.HIGH.name().toLowerCase();
        } else if (speedPercentage >= thresholds.getNeutral()) {
            return SpeedDisplayClass.NEUTRAL.name().toLowerCase();
        } else if (speedPercentage >= thresholds.getLow()) {
            return SpeedDisplayClass.LOW.name().toLowerCase();
        }
        return null;
    }

    @Setter
    @Getter
    public static class SpeedDisplayThresholds {
        private float high;
        private float neutral;
        private float low;
    }

}
