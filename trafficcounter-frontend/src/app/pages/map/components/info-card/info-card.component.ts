/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {Component, Input, OnInit} from '@angular/core';
import {GeoJsonPropertiesDto} from "../../../../api/models/geo-json-properties-dto";

@Component({
  selector: 'app-info-card',
  templateUrl: './info-card.component.html',
  styleUrls: ['./info-card.component.scss']
})
export class InfoCardComponent implements OnInit {

  @Input('info')
  public info!: GeoJsonPropertiesDto

  constructor() {
  }

  ngOnInit(): void {

  }
}
