/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {Component, Input, OnInit} from '@angular/core';
import {LayerSpecification, Map, MapOptions} from 'maplibre-gl';
import {BehaviorSubject, combineLatestWith, Observable} from "rxjs";
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
  private map!: Map;

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
      'circle-radius': 8
    }
  }

  private readonly measurementPointLayer: LayerSpecification = {
    'id': 'measurementPoints',
    'type': 'symbol',
    'source': 'measurementPoints',
    'layout': {
      'icon-image': 'location-pin-thin',
      'text-offset': [0, 1.25],
      'text-anchor': 'top'
    }
  };

  private readonly vehicleAmountLayer: LayerSpecification = {
    'id': 'vehicleAmount',
    'type': 'circle',
    'source': 'vehicleAmount',
    'paint': {
      'circle-radius': ["*", 0.75, ['get', 'numberOfVehicles', ['get', 'vehicleAmount']]],
      'circle-color': '#d22525',
      'circle-opacity': 0.2
    }
  };

  private readonly vehicleSpeedLayer: LayerSpecification = {
    'id': 'vehicleSpeed',
    'type': 'symbol',
    'source': 'vehicleSpeed',
    'layout': {
      'icon-image': ['concat', 'location-pin-', ['get', 'speedDisplayClass', ['get', 'speedData']]],
      'text-offset': [0, 1.25],
      'text-anchor': 'top'
    }
  };

  constructor(private readonly mapConfigService: MapConfigService) {
  }

  ngOnInit(): void {
    this.constructMap();
    this.mapConfigService.showSidebar$.subscribe(() => {
      setTimeout(() => this.map.resize(), 1);
    });
    this.map.on('load', () => {
      this.measurementPoints$.subscribe(measurementPoints => {
        this.displayMeasurementPoints(measurementPoints);
      });

      this.vehicleData$.subscribe(vehicleData => {

        this.mapConfigService.mapMode$.subscribe(mapMode => {

          if (mapMode === MapMode.VehicleAmount) {
            this.displayVehicleAmount(vehicleData);
          } else {
            this.removeLayerIfExists(this.vehicleAmountLayer);
          }
          if (mapMode == MapMode.VehicleSpeed) {
            this.displayVehicleSpeed(vehicleData);
          } else {
            this.removeLayerIfExists(this.vehicleSpeedLayer);
          }
        });
        this.updateVehicleDataLayer(vehicleData);
      });

      this.measurementPoints$.pipe(
        combineLatestWith(this.vehicleData$)
      ).subscribe(([measurementPoints, vehicleData]) => {
        const mergedVehicleData: GeoJsonFeatureCollectionDto = {
          type: measurementPoints.type,
          features: measurementPoints.features.concat(vehicleData.features)
        }
        this.updateVehicleDataLayer(mergedVehicleData);
      });

    });

  }

  /**
   * Loads the icon to the given id.
   *
   * @param id holds the icon identifier.
   */
  private loadIcon(id: string) {
    const img = new Image(20, 20);
    img.onload = () => this.map.addImage(id, img);
    img.src = `./assets/icons/${id}.svg`;
  }

  /**
   * Creates the map object and loads all needed icons.
   */
  private constructMap() {
    this.map = new Map(this.mapOptions);
    this.icons.forEach(icon => this.loadIcon(icon));
  }

  /**
   * This method handles the display of new measurement points.
   *
   * @param measurementPoints holds the features as a GeoJson representation.
   */
  private displayMeasurementPoints(measurementPoints: GeoJsonFeatureCollectionDto): void {
    this.updateLayer(this.measurementPointLayer, measurementPoints);
  }

  /**
   * This method handles the display of new vehicle data.
   *
   * @param vehicleData holds the features as a GeoJson representation.
   */
  private updateVehicleDataLayer(vehicleData: GeoJsonFeatureCollectionDto): void {
    this.updateLayer(this.vehicleDataLayer, vehicleData);

    this.map.on('mouseenter', this.vehicleDataLayer.id, () => {
      this.map.getCanvas().style.cursor = 'pointer';
    });

    this.map.on('mouseleave', this.vehicleDataLayer.id, () => {
      this.map.getCanvas().style.cursor = '';
    });

    this.map.on('click', this.vehicleDataLayer.id, (e: any) => {
      const coordinates = e.features[0].geometry['coordinates'].slice();

      this.mapConfigService.showSidebar$ = new BehaviorSubject<boolean>(true);

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
        speedData: rawProperties.speedData !== undefined ? JSON.parse(rawProperties.speedData) : null,
        vehicleAmount: rawProperties.vehicleAmount !== undefined ? JSON.parse(rawProperties.vehicleAmount) : null
      }
      this.setSelectedPointInfo(properties as GeoJsonPropertiesDto);
    });
  }

  /**
   * This method is used to update the vehicle amount data shown in the map.
   *
   * @param vehicleAmount holds the features used for displaying the vehicle amount.
   */
  private displayVehicleAmount(vehicleAmount: GeoJsonFeatureCollectionDto): void {
    this.updateLayer(this.vehicleAmountLayer, vehicleAmount);
  }

  /**
   * This method is used to update the vehicle speed data shown in the map.
   *
   * @param vehicleSpeed holds the features used for displaying the vehicle speed.
   */
  private displayVehicleSpeed(vehicleSpeed: GeoJsonFeatureCollectionDto): void {
    this.updateLayer(this.vehicleSpeedLayer, vehicleSpeed);
  }

  /**
   * Removes a layer from the map - if the layer exists.
   *
   * @param layer holds the layer specification to find the layer (uses the id).
   */
  private removeLayerIfExists(layer: LayerSpecification) {
    const layerExists = this.map.getLayer(layer.id) !== undefined;
    if (layerExists) {
      this.map.removeLayer(layer.id);
    }

    const sourceExists = this.map.getSource(layer.id) !== undefined;
    if (sourceExists) {
      this.map.removeSource(layer.id);
    }
  }

  /**
   * This will update a used layer with new data.
   *
   * @param layer holds the layer specification.
   * @param data holds the data that will be displayed on the layer.
   */
  private updateLayer(layer: LayerSpecification, data: GeoJsonFeatureCollectionDto) {
    this.removeLayerIfExists(layer);

    this.map.addSource(layer.id, {
      type: 'geojson',
      data: data
    });

    this.map.addLayer(layer);
  }

  /**
   * This method is called if the user clicks on a point. It will save the selected point so other components can use it further.
   *
   * @param selectedPointInfo holds the data of the selected measurement point.
   */
  private setSelectedPointInfo(selectedPointInfo: GeoJsonPropertiesDto) {
    this.mapConfigService.selectedPointInfo$.next(selectedPointInfo);
  }
}
