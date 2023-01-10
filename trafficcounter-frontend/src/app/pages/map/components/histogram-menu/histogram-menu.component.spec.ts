/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistogramMenuComponent } from './histogram-menu.component';
import {MaterialModule} from "../../../../material/material.module";
import {TranslateModule} from "@ngx-translate/core";

describe('HistogramMenuComponent', () => {
  let component: HistogramMenuComponent;
  let fixture: ComponentFixture<HistogramMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HistogramMenuComponent ],
      imports: [
        MaterialModule,
        TranslateModule.forRoot()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistogramMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
