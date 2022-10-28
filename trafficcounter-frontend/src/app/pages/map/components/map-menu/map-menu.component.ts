import { Component, OnInit } from '@angular/core';
import {MapConfigService} from "../../services/map-config/map-config.service";

@Component({
  selector: 'app-map-menu',
  templateUrl: './map-menu.component.html',
  styleUrls: ['./map-menu.component.scss']
})
export class MapMenuComponent implements OnInit {

  constructor(private readonly mapConfigService: MapConfigService) { }

  ngOnInit(): void {
  }

}
