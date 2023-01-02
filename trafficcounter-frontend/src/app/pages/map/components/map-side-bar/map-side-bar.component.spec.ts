/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MapSideBarComponent} from './map-side-bar.component';
import {MaterialModule} from "../../../../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
import {HttpClientModule} from "@angular/common/http";
import {ApiModule} from "../../../../api/api.module";

describe('MapSideBarComponent', () => {
  let component: MapSideBarComponent;
  let fixture: ComponentFixture<MapSideBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MapSideBarComponent],
      imports: [
        MaterialModule,
        TranslateModule.forRoot(),
        HttpClientModule,
        ApiModule,
      ]
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
