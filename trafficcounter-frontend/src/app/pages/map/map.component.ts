import {Component, OnInit} from '@angular/core';
import {MeasurementPointService} from "../../services/measurement-point/measurement-point.service";
import {Observable} from "rxjs";
import {GeoJsonFeatureCollectionDto} from "../../api/models/geo-json-feature-collection-dto";

/**
 * The page component for the map.
 */
@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

  /**
   * All measurement points as GeoJSON.
   */
  measurementPoints: Observable<GeoJsonFeatureCollectionDto>;

  constructor(
    private readonly measurementPointService: MeasurementPointService
  ) {
    this.measurementPoints = this.measurementPointService.getAllMeasurementPoints();
  }

  ngOnInit(): void {
  }

}
