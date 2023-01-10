/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {MapRoutingModule} from './map-routing.module';
import {MapComponent} from './map.component';
import {MapDisplayComponent} from './components/map-display/map-display.component';
import {MaterialModule} from "../../material/material.module";
import {MapMenuBarComponent} from "./components/map-menu-bar/map-menu-bar.component";
import {TranslateModule} from "@ngx-translate/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InfoCardComponent} from './components/info-card/info-card.component';
import {MapSideBarComponent} from "./components/map-side-bar/map-side-bar.component";
import { HistogramComponent } from './components/histogram/histogram.component';
import { HistogramMenuComponent } from './components/histogram-menu/histogram-menu.component';
import { MapOptionsComponent } from './components/map-options/map-options.component';


@NgModule({
  declarations: [
    MapComponent,
    MapDisplayComponent,
    MapMenuBarComponent,
    MapSideBarComponent,
    InfoCardComponent,
    HistogramComponent,
    HistogramMenuComponent,
    MapOptionsComponent
  ],
  imports: [
    CommonModule,
    MapRoutingModule,
    MaterialModule,
    TranslateModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class MapModule {
}
