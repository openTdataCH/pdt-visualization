/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {InfoCardComponent} from './info-card.component';
import {MaterialModule} from "../../../../material/material.module";
import {TranslateModule} from "@ngx-translate/core";

describe('InfoCardComponent', () => {
  let component: InfoCardComponent;
  let fixture: ComponentFixture<InfoCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InfoCardComponent],
      imports: [
        MaterialModule,
        TranslateModule.forRoot()
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(InfoCardComponent);
    component = fixture.componentInstance;
    component.info = {
      id: '1',
      speedData: {
        averageSpeed: 100,
        estimatedSpeedLimit: 80,
        speedDisplayClass: 'high'
      },
      vehicleAmount: {
        numberOfVehicles: 1
      }
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
