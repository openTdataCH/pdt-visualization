import {Component, OnInit} from '@angular/core';
import {MapConfigService} from "../../services/map-config/map-config.service";
import {Observable} from "rxjs";

/**
 * Menu bar on top of the map content.
 */
@Component({
  selector: 'app-map-menu-bar',
  templateUrl: './map-menu-bar.component.html',
  styleUrls: ['./map-menu-bar.component.scss']
})
export class MapMenuBarComponent implements OnInit {

  isMobile$!: Observable<boolean>

  constructor(private readonly mapConfigService: MapConfigService) {
    this.isMobile$ = mapConfigService.isMobile$;
  }

  ngOnInit(): void {
  }

  /**
   * Toggles the menu display.
   */
  toggleShowSidebar() {
    this.mapConfigService.showSidebar$.next(!this.mapConfigService.showSidebar$.value);
  }



}
