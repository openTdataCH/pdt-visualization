import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MapRoutingModule } from './map-routing.module';
import { MapComponent } from './map.component';
import { MapDisplayComponent } from './components/map-display/map-display.component';


@NgModule({
  declarations: [
    MapComponent,
    MapDisplayComponent
  ],
  imports: [
    CommonModule,
    MapRoutingModule
  ]
})
export class MapModule { }
