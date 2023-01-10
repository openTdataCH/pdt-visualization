/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.config;

import ch.opentdata.wsdl.PullService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenTransportData API.
 *
 * @author Manuel Riesen
 */
@Configuration
public class OpenTransportDataApiConfig {
    @Bean
    public PullService pullService() {
        return new PullService();
    }
}
