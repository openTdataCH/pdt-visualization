import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {ApiModule} from "./api/api.module";
import {HttpClientModule, HTTP_INTERCEPTORS} from "@angular/common/http";
import { ErrorDisplayComponent } from './components/error-display/error-display.component';
import {ErrorInterceptor} from "./interceptors/error.interceptor";
import {ToastrModule} from "ngx-toastr";
import {MaterialModule} from "./material/material.module";
import { HeaderComponent } from './components/header/header.component';

@NgModule({
  declarations: [
    AppComponent,
    ErrorDisplayComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ApiModule,
    MaterialModule,
    ToastrModule.forRoot({
      timeOut: 5000,
      preventDuplicates: true
    })
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
