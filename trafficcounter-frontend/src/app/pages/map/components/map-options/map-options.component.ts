/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import { Component, OnInit } from '@angular/core';
import {MapConfigService} from "../../services/map-config/map-config.service";
import {FormControl} from "@angular/forms";
import {MapMode} from "../../models/map-mode";

@Component({
  selector: 'app-map-options',
  templateUrl: './map-options.component.html',
  styleUrls: ['./map-options.component.scss']
})
export class MapOptionsComponent implements OnInit {

  modeControl = new FormControl(MapMode.MeasurementPoints);
  constructor(private readonly mapConfigService: MapConfigService) { }

  ngOnInit(): void {
  }


  updateMode(): void {
    this.mapConfigService.mapMode$.next(this.modeControl.value ?? MapMode.MeasurementPoints);
  }

}
