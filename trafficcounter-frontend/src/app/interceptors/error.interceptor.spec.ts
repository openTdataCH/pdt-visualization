/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {TestBed} from '@angular/core/testing';

import {ErrorInterceptor} from './error.interceptor';
import {AppModule} from "../app.module";

describe('ErrorInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [ErrorInterceptor],
    imports: [AppModule]
  }));

  it('should be created', () => {
    const interceptor: ErrorInterceptor = TestBed.inject(ErrorInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
