import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MapSideBarComponent} from './map-side-bar.component';
import {MaterialModule} from "../../../../material/material.module";
import {TranslateModule} from "@ngx-translate/core";

describe('MapMenuComponent', () => {
  let component: MapSideBarComponent;
  let fixture: ComponentFixture<MapSideBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MapSideBarComponent],
      imports: [MaterialModule, TranslateModule.forRoot()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MapSideBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
