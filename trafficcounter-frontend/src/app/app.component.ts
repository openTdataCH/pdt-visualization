import { Component } from '@angular/core';
import {GeoJsonService} from "./services/geo-json/geo-json.service";
import {MeasurementPointDto} from "./models/api/measurement-point-dto.model";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'trafficcounter-frontend';

  constructor(private geoJsonService: GeoJsonService) {
  }

  generate() {
    const measurementPoints: MeasurementPointDto[] = [];
    for (let i = 0; i < 2500; i++) {
      measurementPoints.push({
        id: "Point-" + Math.random(),
        longitude: Math.random() + 8.1336,
        latitude: Math.random() + 46.784,
      })
    }

  }
}
