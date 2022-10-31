import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MapRoutingModule } from './map-routing.module';
import { MapComponent } from './map.component';
import { MapDisplayComponent } from './components/map-display/map-display.component';
import {MaterialModule} from "../../material/material.module";
import {MapMenuBarComponent} from "./components/map-menu-bar/map-menu-bar.component";
import { MapMenuComponent } from './components/map-menu/map-menu.component';


@NgModule({
  declarations: [
    MapComponent,
    MapDisplayComponent,
    MapMenuBarComponent,
    MapMenuComponent
  ],
  imports: [
    CommonModule,
    MapRoutingModule,
    MaterialModule
  ]
})
export class MapModule { }
