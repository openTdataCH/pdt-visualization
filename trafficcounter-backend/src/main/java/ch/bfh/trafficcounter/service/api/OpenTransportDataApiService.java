/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.service.api;

import ch.opentdata.wsdl.D2LogicalModel;

/**
 * Service for consuming the OpenTransportData API.
 *
 * @author Manuel Riesen
 */
public interface OpenTransportDataApiService {

    /**
     * Pulls measured data from the API.
     *
     * @return measured data
     */
    D2LogicalModel pullMeasuredData();

    /**
     * Pulls the measurement site table from the API.
     *
     * @return measurement site table
     */
    D2LogicalModel pullMeasurementSiteTable();
}
