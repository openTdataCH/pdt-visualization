package ch.bfh.trafficcounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrafficcounterApplication {

    public static void main(String[] args) {

        // set protocolHandler to custom handler for WSDL
        System.setProperty("java.protocol.handler.pkgs", "ch.bfh.trafficcounter.protocols");

        SpringApplication.run(TrafficcounterApplication.class, args);
    }

}
