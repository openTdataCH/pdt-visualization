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

  constructor() { }

  ngOnInit(): void {

  }

}
