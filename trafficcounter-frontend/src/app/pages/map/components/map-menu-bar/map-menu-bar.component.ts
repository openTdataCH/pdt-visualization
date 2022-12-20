import {Component, OnInit} from '@angular/core';
import {MapConfigService} from "../../services/map-config/map-config.service";
import {FormControl} from "@angular/forms";
import {MapMode} from "../../models/map-mode";

/**
 * Menu bar on top of the map content.
 */
@Component({
  selector: 'app-map-menu-bar',
  templateUrl: './map-menu-bar.component.html',
  styleUrls: ['./map-menu-bar.component.scss']
})
export class MapMenuBarComponent implements OnInit {

  modeControl = new FormControl(MapMode.MeasurementPoints);

  constructor(private readonly mapConfigService: MapConfigService) {
  }

  ngOnInit(): void {
  }

  /**
   * Toggles the menu display.
   */
  toggleShowSidebar() {
    this.mapConfigService.showSidebar$.next(!this.mapConfigService.showSidebar$.value);
  }


  updateMode(): void {
    this.mapConfigService.mapMode$.next(this.modeControl.value ?? MapMode.MeasurementPoints);
  }

}
