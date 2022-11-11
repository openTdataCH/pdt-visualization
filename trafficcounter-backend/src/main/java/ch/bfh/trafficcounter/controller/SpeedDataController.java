package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.model.dto.geojson.SpeedDataDto;
import ch.bfh.trafficcounter.service.SpeedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/speeddata")
public class SpeedDataController {

    private final SpeedDataService speedDataService;

    @Autowired
    public SpeedDataController(SpeedDataService speedDataService) {
        this.speedDataService = speedDataService;
    }

    @GetMapping
    public ResponseEntity<List<SpeedDataDto>> getCurrentSpeedData() {
        return ResponseEntity.ok(speedDataService.getCurrentSpeedData());
    }
}
