import { Component, OnInit } from '@angular/core';
import {MapConfigService} from "../../services/map-config/map-config.service";
import {MatButtonToggleChange} from "@angular/material/button-toggle";
import {$} from "chart.js/dist/chunks/helpers.core";

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
