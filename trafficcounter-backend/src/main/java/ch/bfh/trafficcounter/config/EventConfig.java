package ch.bfh.trafficcounter.config;

import ch.bfh.trafficcounter.event.UpdateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

/**
 * Configuration for event handling.
 *
 * @author Manuel Riesen
 */
@Configuration
public class EventConfig {

    @Bean
    public Sinks.Many<UpdateEvent> updateEvent() {
        return Sinks.many().replay().latest();
    }
}
