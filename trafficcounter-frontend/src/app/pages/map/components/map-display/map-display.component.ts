import {Component, Input, OnInit} from '@angular/core';
import {LayerSpecification, Map, MapOptions, Popup} from 'maplibre-gl';
import {Observable, Subscription} from "rxjs";
import {GeoJsonFeatureCollectionDto} from "../../../../api/models/geo-json-feature-collection-dto";
import {MapConfigService} from "../../services/map-config/map-config.service";
import {MapMode} from "../../models/map-mode";

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
   * Amount of vehicles to display on the map.
   */
  @Input('vehicle-amount')
  vehicleAmount$!: Observable<GeoJsonFeatureCollectionDto>;

  private vehicleAmountSubscription: Subscription | null = null;

  private readonly icons: Array<string> = [
    'location-pin-thin'
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
    'type': 'symbol',
    'source': 'vehicleAmount',
    'layout': {
      'icon-image': 'location-pin-thin',
      'text-field': ['get', 'vehicleAmount.numberOfVehicles'],
      'text-offset': [0, 1.25],
      'text-anchor': 'top'
    }
  };

  constructor(private readonly mapConfigService: MapConfigService) {
  }

  private loadIcon(id: string) {
    const img = new Image(20, 20);
    img.onload = () => this.map.addImage(id, img);
    img.src = `./assets/icons/${id}.svg`;
  }

  ngOnInit(): void {
    this.constructMap();
    this.mapConfigService.showMenu$.subscribe(showMenu => {
      setTimeout(() => this.map.resize(), 1);
    });
    this.mapConfigService.mapMode$.subscribe(mapMode => {
      if(mapMode === MapMode.VehicleAmount) {
        console.log('vehicle amount mode');
        this.vehicleAmountSubscription = this.vehicleAmount$.subscribe(vehicleAmount => {
          //TODO handle vehicle amount
          this.displayVehicleAmount(vehicleAmount);
        });
      } else {
        this.vehicleAmountSubscription?.unsubscribe();
      }
    });
  }

  private constructMap() {
    this.map = new Map(this.mapOptions);
    this.icons.forEach(icon => this.loadIcon(icon));

    // wait for map to load
    this.map.on('load', () => {

      this.measurementPoints$.subscribe(measurementPoints => this.displayMeasurementPoints(measurementPoints));
    });
  }

  private displayMeasurementPoints(measurementPoints: GeoJsonFeatureCollectionDto): void {
    this.updateLayer(this.measurementPointLayer, measurementPoints);

    const handleMeasurementPointPopup = (e: any) => {
      const coordinates = e.features[0].geometry['coordinates'].slice();
      const description = e.features[0].properties['id'];

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

    // add event listener for measurement points
    this.map.on('click', this.measurementPointLayer.id, handleMeasurementPointPopup);
    this.addDefaultListeners(this.measurementPointLayer.id);
  }


  private displayVehicleAmount(vehicleAmount: GeoJsonFeatureCollectionDto): void {
    this.updateLayer(this.vehicleAmountLayer, vehicleAmount);

    // add event listener for measurement points
    this.addDefaultListeners(this.measurementPointLayer.id);
  }

  private updateLayer(layer: LayerSpecification, data: GeoJsonFeatureCollectionDto) {
    const sourceExists = this.map.getSource(layer.id) !== undefined;
    if(sourceExists) {
      this.map.removeSource(layer.id);
    }
    this.map.addSource(layer.id, {
      type: 'geojson',
      data: data
    });
    const layerExists = this.map.getLayer(layer.id) !== undefined;
    if(layerExists) {
      this.map.removeLayer(layer.id);
    }
    this.map.addLayer(layer);
  }

  private addDefaultListeners(layerId: string) {
    this.map.on('mouseenter', layerId, () => {
      this.map.getCanvas().style.cursor = 'pointer';
    });
    this.map.on('mouseleave', layerId, () => {
      this.map.getCanvas().style.cursor = '';
    });
  }

}



