/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class TrafficcounterApplicationTests {

    public TrafficcounterApplicationTests() {
        // set protocolHandler to custom handler for WSDL
        System.setProperty("java.protocol.handler.pkgs", "ch.bfh.trafficcounter.protocols");
    }

    @Test
    void contextLoads() {
    }

}
