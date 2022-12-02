import {Component, Input, OnInit} from '@angular/core';
import {LayerSpecification, Map, MapOptions, Popup} from 'maplibre-gl';
import {Observable} from "rxjs";
import {GeoJsonFeatureCollectionDto} from "../../../../api/models/geo-json-feature-collection-dto";
import {MapConfigService} from "../../services/map-config/map-config.service";
import {MapMode} from "../../models/map-mode";
import {GeoJsonPropertiesDto} from "../../../../api/models/geo-json-properties-dto";

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
  measurementPoints$!: Observable<GeoJsonFeatureCollectionDto>;

  /**
   * Vehicle data to display on the map.
   */
  @Input('vehicle-data')
  vehicleData$!: Observable<GeoJsonFeatureCollectionDto>;

  private readonly icons: Array<string> = [
    'location-pin-thin',
    'location-pin-high',
    'location-pin-neutral',
    'location-pin-low'
  ];

  private readonly mapOptions: MapOptions = {
    container: 'map',
    trackResize: true,
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

  private readonly vehicleDataLayer: LayerSpecification = {
    'id': 'vehicleData',
    'type': 'circle',
    'source': 'vehicleData',
    'layout': {
      'visibility': 'visible'
    },
    'paint': {
      'circle-color': 'blue',
      'circle-opacity': 0,
      'circle-radius': 5
    }
  }

  private readonly measurementPointLayer: LayerSpecification = {
    'id': 'measurementPoints',
    'type': 'symbol',
    'source': 'measurementPoints',
    'layout': {
      'icon-image': 'location-pin-thin',
      //'text-field': ['get', 'id'],
      'text-offset': [0, 1.25],
      'text-anchor': 'top'
    }
  };

  private readonly vehicleAmountLayer: LayerSpecification = {
    'id': 'vehicleAmount',
    'type': 'circle',
    'source': 'vehicleAmount',
    'paint': {
      'circle-radius': [ "*", 0.5, ['get', 'numberOfVehicles', ['get', 'vehicleAmount'] ] ],
      'circle-color': '#d22525',
      'circle-opacity': 0.2
    }
  };

  private readonly vehicleSpeedLayer: LayerSpecification = {
    'id': 'vehicleSpeed',
    'type': 'symbol',
    'source': 'vehicleSpeed',
    'layout': {
      'icon-image': [ 'concat', 'location-pin-', ['get', 'speedDisplayClass', ['get', 'speedData'] ] ],
      'text-offset': [0, 1.25],
      'text-anchor': 'top'
    }
  };

  constructor(private readonly mapConfigService: MapConfigService) {}

  private loadIcon(id: string) {
    const img = new Image(20, 20);
    img.onload = () => this.map.addImage(id, img);
    img.src = `./assets/icons/${id}.svg`;
  }

  ngOnInit(): void {
    this.constructMap();
    this.mapConfigService.showSidebar$.subscribe(showMenu => {
      setTimeout(() => this.map.resize(), 1);
    });
    this.map.on('load', () => {
      this.measurementPoints$.subscribe(measurementPoints => {
        this.displayMeasurementPoints(measurementPoints);
      });
    });
    this.vehicleData$.subscribe(vehicleData => {
      console.log('got vehicle data');

      this.mapConfigService.mapMode$.subscribe(mapMode => {
        console.log('update');

        if(mapMode === MapMode.VehicleAmount) {
          this.displayVehicleAmount(vehicleData);
        } else {
          this.removeLayerIfExists(this.vehicleAmountLayer);
        }
        if(mapMode == MapMode.VehicleSpeed) {
            this.displayVehicleSpeed(vehicleData);
        } else {
          this.removeLayerIfExists(this.vehicleSpeedLayer);
        }
      });
      this.updateVehicleDataLayer(vehicleData);
    });
  }

  private constructMap() {
    this.map = new Map(this.mapOptions);
    this.icons.forEach(icon => this.loadIcon(icon));
  }

  private displayMeasurementPoints(measurementPoints: GeoJsonFeatureCollectionDto): void {
    this.updateLayer(this.measurementPointLayer, measurementPoints);

    //TODO remove if not used
    /*
    this.map.on('mouseenter', this.measurementPointLayer.id, () => {
      this.map.getCanvas().style.cursor = 'not-allowed';
    });
    this.map.on('mouseleave', this.measurementPointLayer.id, () => {
      this.map.getCanvas().style.cursor = '';
    });*/
  }

  private updateVehicleDataLayer(vehicleData: GeoJsonFeatureCollectionDto): void {
    this.updateLayer(this.vehicleDataLayer, vehicleData);

    this.map.on('click', this.vehicleDataLayer.id, (e: any) => {
      const coordinates = e.features[0].geometry['coordinates'].slice();

      this.map.on('mouseenter', this.vehicleDataLayer.id, () => {
        this.map.getCanvas().style.cursor = 'pointer';
      });
      this.map.on('mouseleave', this.vehicleDataLayer.id, () => {
        this.map.getCanvas().style.cursor = '';
      });

      // Ensure that if the map is zoomed out such that multiple
      // copies of the feature are visible, the popup appears
      // over the copy being pointed to.
      // See: https://maplibre.org/maplibre-gl-js-docs/example/popup-on-click/
      while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
        coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
      }
      const rawProperties = e.features[0].properties;
      const properties: GeoJsonPropertiesDto = {
        id: rawProperties.id,
        speedData: JSON.parse(rawProperties.speedData),
        vehicleAmount: JSON.parse(rawProperties.vehicleAmount)
      }
      this.setSelectedPointInfo(properties as GeoJsonPropertiesDto);
    });
  }

  private displayVehicleAmount(vehicleAmount: GeoJsonFeatureCollectionDto): void {
    this.updateLayer(this.vehicleAmountLayer, vehicleAmount);
  }

  private displayVehicleSpeed(vehicleSpeed: GeoJsonFeatureCollectionDto): void {
    this.updateLayer(this.vehicleSpeedLayer, vehicleSpeed);
  }

  private removeLayerIfExists(layer: LayerSpecification) {
    const layerExists = this.map.getLayer(layer.id) !== undefined;
    if(layerExists) {
      this.map.removeLayer(layer.id);
    }
    const sourceExists = this.map.getSource(layer.id) !== undefined;
    if(sourceExists) {
      this.map.removeSource(layer.id);
    }
  }

  private updateLayer(layer: LayerSpecification, data: GeoJsonFeatureCollectionDto) {

    this.removeLayerIfExists(layer);

    this.map.addSource(layer.id, {
      type: 'geojson',
      data: data
    });

    this.map.addLayer(layer);
  }

  private setSelectedPointInfo(selectedPointInfo: GeoJsonPropertiesDto) {
    this.mapConfigService.selectedPointInfo$.next(selectedPointInfo);
  }

  private clearSelectedPointInfo() {
    this.mapConfigService.selectedPointInfo$.next(null);
  }

}



