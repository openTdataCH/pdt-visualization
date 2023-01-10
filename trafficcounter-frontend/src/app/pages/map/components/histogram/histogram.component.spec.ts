/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistogramComponent } from './histogram.component';
import {HttpClientModule} from "@angular/common/http";
import {ApiModule} from "../../../../api/api.module";
import {TranslateModule} from "@ngx-translate/core";
import {of} from "rxjs";

describe('HistogramComponent', () => {
  let component: HistogramComponent;
  let fixture: ComponentFixture<HistogramComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HistogramComponent ],
      imports: [
        HttpClientModule,
        ApiModule,
        TranslateModule.forRoot()
      ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistogramComponent);
    component = fixture.componentInstance;
    component.duration$ = of('24h');
    component.data$ = of(null);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
