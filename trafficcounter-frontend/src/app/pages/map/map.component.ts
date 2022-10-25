import { Component, OnInit } from '@angular/core';
import {MeasurementPointService} from "../../services/measurement-point/measurement-point.service";
import {catchError, map, Observable} from "rxjs";
import {GeoJsonFeatureCollectionDto} from "../../api/models/geo-json-feature-collection-dto";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

  measurementPoints: Observable<GeoJsonFeatureCollectionDto>;

  constructor(
    private readonly measurementPointService: MeasurementPointService
  ) {
    this.measurementPoints = this.measurementPointService.getAllMeasurementPoints();
  }

  ngOnInit(): void {
  }

}
