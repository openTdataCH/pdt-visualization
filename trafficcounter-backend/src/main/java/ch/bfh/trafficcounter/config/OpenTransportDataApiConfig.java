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
