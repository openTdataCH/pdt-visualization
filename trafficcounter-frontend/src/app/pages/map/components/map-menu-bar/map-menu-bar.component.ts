import { Component, OnInit } from '@angular/core';
import {MapConfigService} from "../../services/map-config/map-config.service";

@Component({
  selector: 'app-map-menu-bar',
  templateUrl: './map-menu-bar.component.html',
  styleUrls: ['./map-menu-bar.component.scss']
})
export class MapMenuBarComponent implements OnInit {

  constructor(private readonly mapConfigService: MapConfigService) { }

  ngOnInit(): void {
  }

  toggleShowMenu() {
    this.mapConfigService.showMenu.next(!this.mapConfigService.showMenu.value);
  }

}
