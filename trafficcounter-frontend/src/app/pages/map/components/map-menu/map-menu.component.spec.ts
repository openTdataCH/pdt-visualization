import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MapMenuComponent } from './map-menu.component';
import {MaterialModule} from "../../../../material/material.module";
import {TranslateModule} from "@ngx-translate/core";

describe('MapMenuComponent', () => {
  let component: MapMenuComponent;
  let fixture: ComponentFixture<MapMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MapMenuComponent ],
      imports: [ MaterialModule, TranslateModule.forRoot() ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MapMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
