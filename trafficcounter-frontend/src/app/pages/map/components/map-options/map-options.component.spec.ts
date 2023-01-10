/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MaterialModule } from 'src/app/material/material.module';

import { MapOptionsComponent } from './map-options.component';
import {TranslateModule} from "@ngx-translate/core";

describe('MapOptionsComponent', () => {
  let component: MapOptionsComponent;
  let fixture: ComponentFixture<MapOptionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MapOptionsComponent ],
      imports: [
        MaterialModule,
        TranslateModule.forRoot()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MapOptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
