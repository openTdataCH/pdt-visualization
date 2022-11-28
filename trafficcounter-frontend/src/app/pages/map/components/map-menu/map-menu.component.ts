import {Component, OnInit} from '@angular/core';
import {MapConfigService} from "../../services/map-config/map-config.service";
import {FormControl} from "@angular/forms";
import {MapMode} from "../../models/map-mode";

/**
 * The menu for map options.
 */
@Component({
  selector: 'app-map-menu',
  templateUrl: './map-menu.component.html',
  styleUrls: ['./map-menu.component.scss']
})
export class MapMenuComponent implements OnInit {

  modeControl = new FormControl(MapMode.MeasurementPoints);

  constructor(private readonly mapConfigService: MapConfigService) { }

  ngOnInit(): void {

  }

  updateMode(): void {
    this.mapConfigService.mapMode$.next(this.modeControl.value ?? MapMode.MeasurementPoints);
  }

}
