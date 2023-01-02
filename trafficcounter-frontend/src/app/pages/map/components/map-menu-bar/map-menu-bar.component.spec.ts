/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

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
