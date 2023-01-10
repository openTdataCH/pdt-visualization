/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

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
