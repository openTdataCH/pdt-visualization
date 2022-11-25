import {TestBed} from '@angular/core/testing';

import {ErrorService} from './error.service';
import {AppModule} from "../../app.module";

describe('ErrorService', () => {
  let service: ErrorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ AppModule ]
    });
    service = TestBed.inject(ErrorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
