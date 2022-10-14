import { Component, OnInit } from '@angular/core';
import { LngLat, Map } from 'maplibre-gl';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

  mapLibre!: Map;

  constructor() { }

  ngOnInit(): void {
    this.mapLibre = new Map({
      container: 'map',
      style: 'https://vectortiles.geo.admin.ch/styles/ch.swisstopo.leichte-basiskarte_world.vt/style.json', // stylesheet location
      center: [8.1336, 46.784], // starting position [lng, lat]
      zoom: 7.5 // starting zoom
    });
  }

}
