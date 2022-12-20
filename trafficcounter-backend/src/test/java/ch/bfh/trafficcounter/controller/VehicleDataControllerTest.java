package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.model.entity.MeasurementStatsType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class VehicleDataControllerTest extends AbstractApiTest {

    @Test
    void testGetVehicleAmount() {
        request()
            .get("/api/vehicledata")
            .then()
            .assertThat()
            .body("features.size()", is(1))
            .body("features[0].properties.speedData.averageSpeed", equalTo(10f))
            .body("features[0].properties.speedData.speedDisplayClass", equalTo("high"))
            .body("features[0].properties.vehicleAmount.numberOfVehicles", equalTo(1))
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testGetHistoricVehicleData24h() {
        request()
            .get("/api/vehicledata/history/CH-TEST?duration=24h")
            .then()
            .assertThat()
            .body("resolution", equalTo(MeasurementStatsType.HOURLY.getDuration()))
            .body("measurements[0].avgVehicleAmount", equalTo(1))
            .body("measurements[0].avgVehicleSpeed", equalTo(10f))
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testGetHistoricVehicleData7d() {
        request()
            .get("/api/vehicledata/history/CH-TEST?duration=7d")
            .then()
            .assertThat()
            .body("resolution", equalTo(MeasurementStatsType.DAILY.getDuration()))
            .body("measurements[0].avgVehicleAmount", equalTo(1))
            .body("measurements[0].avgVehicleSpeed", equalTo(10f))
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testGetHistoricVehicleData30d_negative() {
        request()
            .get("/api/vehicledata/history/CH-TEST?duration=30d")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void testGetHistoricVehicleDataWrongEndpoint_negative() {
        request()
            .get("/api/vehicledata/history/CH-TESTX?duration=24h")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

}
