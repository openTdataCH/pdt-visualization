import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MapMenuBarComponent} from './map-menu-bar.component';
import {MaterialModule} from "../../../../material/material.module";
import {TranslateModule} from "@ngx-translate/core";

describe('MapMenuBarComponent', () => {
  let component: MapMenuBarComponent;
  let fixture: ComponentFixture<MapMenuBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MapMenuBarComponent],
      imports: [
        MaterialModule,
        TranslateModule.forRoot()
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MapMenuBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
