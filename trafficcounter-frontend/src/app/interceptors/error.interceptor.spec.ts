import {TestBed} from '@angular/core/testing';

import {ErrorInterceptor} from './error.interceptor';
import {AppModule} from "../app.module";

describe('ErrorInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [ ErrorInterceptor ],
    imports: [ AppModule ]
  }));

  it('should be created', () => {
    const interceptor: ErrorInterceptor = TestBed.inject(ErrorInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
