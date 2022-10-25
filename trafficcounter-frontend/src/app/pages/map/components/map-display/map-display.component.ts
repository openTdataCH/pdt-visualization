import {Component, Input, OnInit} from '@angular/core';
import {LayerSpecification, Map, MapOptions, Popup} from 'maplibre-gl';
import {Observable} from "rxjs";
import {GeoJsonFeatureCollectionDto} from "../../../../api/models/geo-json-feature-collection-dto";

/**
 * Component for the map display.
 * Encapsulates the map view and handles its data to display.
 */
@Component({
  selector: 'app-map-display',
  templateUrl: './map-display.component.html',
  styleUrls: ['./map-display.component.scss']
})
export class MapDisplayComponent implements OnInit {

  private map!: Map;

  /**
   * Measurement points to display on the map.
   */
  @Input('measurement-points')
  measurementPoints!: Observable<GeoJsonFeatureCollectionDto>;

  private readonly icons: Array<string> = [
    'location-pin-thin'
  ];

  private readonly mapOptions: MapOptions = {
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
  }

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

  private loadIcon(id: string) {
    const img = new Image(20, 20)
    img.onload = () => this.map.addImage(id, img)
    img.src = './assets/icons/location-pin-thin.svg';
  }

  ngOnInit(): void {
    this.map = new Map(this.mapOptions);
    this.icons.forEach(this.loadIcon);

    // wait for map to load
    this.map.on('load', () => {

      // wait for measurement points to load
      this.measurementPoints.subscribe(measurementPoints => {
        this.map.addSource(this.measurementPointLayer.id, {
          type: 'geojson',
          data: measurementPoints
        });
        this.map.addLayer(this.measurementPointLayer);

        const handleMeasurementPointPopup = (e: any) => {
          const coordinates = e.features[0].geometry['coordinates'].slice();
          const description = e.features[0].properties['year'];

          // Ensure that if the map is zoomed out such that multiple
          // copies of the feature are visible, the popup appears
          // over the copy being pointed to.
          // See: https://maplibre.org/maplibre-gl-js-docs/example/popup-on-click/
          while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
            coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
          }

          new Popup()
            .setLngLat(coordinates)
            .setHTML(description)
            .addTo(this.map);
        };

        // add event listener for measurment points
        this.map.on('click', this.measurementPointLayer.id, handleMeasurementPointPopup);
        this.map.on('mouseenter', this.measurementPointLayer.id, () => {
          this.map.getCanvas().style.cursor = 'pointer';
        });
        this.map.on('mouseleave', this.measurementPointLayer.id, () => {
          this.map.getCanvas().style.cursor = '';
        });
      });
    });

  }

  }



