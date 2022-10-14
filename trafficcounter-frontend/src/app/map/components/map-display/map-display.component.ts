import {Component, Input, OnInit} from '@angular/core';
import {Map} from 'maplibre-gl';
import {Observable} from "rxjs";
import {GeoJsonFeatureCollection} from "../../../models/geo-json/geo-json-feature-collection.model";
import {MeasurementPointDto} from "../../../models/api/measurement-point-dto.model";

@Component({
  selector: 'app-map-display',
  templateUrl: './map-display.component.html',
  styleUrls: ['./map-display.component.scss']
})
export class MapDisplayComponent implements OnInit {

  mapLibre!: Map;

  @Input('measurement-points')
  measurementPoints!: Observable<GeoJsonFeatureCollection<MeasurementPointDto>>

  constructor() {
  }

  ngOnInit(): void {
    this.mapLibre = new Map({
      container: 'map',
      style: 'https://demotiles.maplibre.org/style.json',
      center: [8.1336, 46.784], // starting position [lng, lat]
      zoom: 7.5 // starting zoom
    });

    this.mapLibre.on('load', () => {
      this.measurementPoints.subscribe(measurementPoints => {
        this.mapLibre.addSource('measurementPoints', {
          type: 'geojson',
          data: measurementPoints
        });
      });

      this.mapLibre.addLayer(
        {
          'id': 'measurementPoints',
          'type': 'circle',
          'source': 'measurementPoints',
        }
      );
    });

  }

}
