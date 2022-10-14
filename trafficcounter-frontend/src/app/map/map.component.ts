import { Component, OnInit } from '@angular/core';
import {MeasurementPointService} from "../services/measurement-point/measurement-point.service";
import {map, Observable} from "rxjs";
import {Marker} from "maplibre-gl";
import {MarkerService} from "../services/marker/marker.service";
import {GeoJsonFeatureCollection} from "../models/geo-json/geo-json-feature-collection.model";
import {MeasurementPointDto} from "../models/api/measurement-point-dto.model";
import {GeoJsonService} from "../services/geo-json/geo-json.service";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

  measurementPoints: Observable<GeoJsonFeatureCollection<MeasurementPointDto>> = this.getMs();

  private getMs(): Observable<GeoJsonFeatureCollection<MeasurementPointDto>> {
    return this.measurementPointService.getAllMeasurementPoints()
      .pipe(map(measurementPoints => this.geoJsonService.convertToFeatureCollection(measurementPoints)));
  }

  constructor(
    private readonly measurementPointService: MeasurementPointService,
    private readonly markerService: MarkerService,
    private readonly geoJsonService: GeoJsonService
  ) { }

  ngOnInit(): void {
  }

}
