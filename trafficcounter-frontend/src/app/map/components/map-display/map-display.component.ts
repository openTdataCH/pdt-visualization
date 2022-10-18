import {Component, Input, OnInit} from '@angular/core';
import {LayerSpecification, Map,} from 'maplibre-gl';
import {Observable} from "rxjs";
import {GeoJsonFeatureCollection} from "../../../models/geo-json/geo-json-feature-collection.model";
import {MeasurementPointDto} from "../../../models/api/measurement-point-dto.model";

@Component({
  selector: 'app-map-display',
  templateUrl: './map-display.component.html',
  styleUrls: ['./map-display.component.scss']
})
export class MapDisplayComponent implements OnInit {

  private map!: Map;

  @Input('measurement-points')
  measurementPoints!: Observable<GeoJsonFeatureCollection<MeasurementPointDto>>

  private readonly measurementPointLayer: LayerSpecification = {
    'id': 'measurementPoints',
    'type': 'symbol',
    'source': 'measurementPoints',
    'layout': {
      'icon-image': 'location-pin-thin',
      'text-field': ['get', 'id'],
      'text-offset': [0, 1.25],
      'text-anchor': 'top'
    }
  };

  constructor() {
  }

  ngOnInit(): void {
    this.map = new Map({
      container: 'map',
      style: {
        glyphs: 'https://demotiles.maplibre.org/font/{fontstack}/{range}.pbf',
        version: 8,
        sources: {
          osm: {
            type: 'raster',
            tiles: ['https://a.tile.openstreetmap.org/{z}/{x}/{y}.png'],
            tileSize: 256,
            attribution: '&copy; OpenStreetMap Contributors',
            maxzoom: 19
          },
        },
        layers: [
          {
            id: 'osm',
            type: 'raster',
            source: 'osm'
          },
        ],
      },
      center: [8.1336, 46.784], // starting position [lng, lat]
      zoom: 7.5 // starting zoom
    });


    this.map.on('load', () => {

      const img = new Image(20, 20)
      img.onload = () => this.map.addImage('location-pin-thin', img)
      img.src = './assets/icons/location-pin-thin.svg';
      this.measurementPoints.subscribe(measurementPoints => {
        this.map.addSource('measurementPoints', {
          type: 'geojson',
          data: measurementPoints
        });
      });

      this.map.addLayer(this.measurementPointLayer);
    });

  }

}
