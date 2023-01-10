/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import { Component, OnInit } from '@angular/core';
import {MapConfigService} from "../../services/map-config/map-config.service";
import {MatButtonToggleChange} from "@angular/material/button-toggle";

@Component({
  selector: 'app-histogram-menu',
  templateUrl: './histogram-menu.component.html',
  styleUrls: ['./histogram-menu.component.scss']
})
export class HistogramMenuComponent implements OnInit {

  constructor(
    private readonly mapConfigService: MapConfigService,
  ) { }

  ngOnInit(): void {
  }

  updateDuration($event: MatButtonToggleChange) {
   this.mapConfigService.histogramDuration$.next($event.value);
  }
}
