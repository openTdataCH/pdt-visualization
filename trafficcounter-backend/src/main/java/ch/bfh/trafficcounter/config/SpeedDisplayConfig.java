package ch.bfh.trafficcounter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "trafficcounter.speed-display")
@Setter
@Getter
public class SpeedDisplayConfig {

    private SpeedDisplayThresholds thresholds;

    @Setter
    private static class SpeedDisplayThresholds {
        private float high;
        private float neutral;
        private float low;
    }

    @PostConstruct
    public void init() {
        System.out.println();
    }

}
