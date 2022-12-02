import {Component, OnInit} from '@angular/core';
import {MapConfigService} from "../../services/map-config/map-config.service";
import {FormControl} from "@angular/forms";
import {MapMode} from "../../models/map-mode";
import {Observable} from "rxjs";
import {GeoJsonPropertiesDto} from "../../../../api/models/geo-json-properties-dto";

/**
 * The menu for map options.
 */
@Component({
  selector: 'app-map-side-bar',
  templateUrl: './map-side-bar.component.html',
  styleUrls: ['./map-side-bar.component.scss']
})
export class MapSideBarComponent implements OnInit {

  modeControl = new FormControl(MapMode.MeasurementPoints);

  selectedPointInfo$: Observable<GeoJsonPropertiesDto | null>;

  constructor(private readonly mapConfigService: MapConfigService) {
    this.selectedPointInfo$ = this.mapConfigService.selectedPointInfo$;
  }

  ngOnInit(): void {
  }

  updateMode(): void {
    this.mapConfigService.mapMode$.next(this.modeControl.value ?? MapMode.MeasurementPoints);
  }

}
