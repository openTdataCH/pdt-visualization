import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ErrorDisplayComponent} from './error-display.component';
import {ToastrModule} from "ngx-toastr";
import {AppModule} from "../../app.module";

describe('ErrorDisplayComponent', () => {
  let component: ErrorDisplayComponent;
  let fixture: ComponentFixture<ErrorDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ErrorDisplayComponent ],
      imports: [ AppModule, ToastrModule.forRoot() ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ErrorDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
