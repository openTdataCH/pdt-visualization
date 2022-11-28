import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ApiModule} from "./api/api.module";
import {HttpClientModule, HTTP_INTERCEPTORS, HttpClient} from "@angular/common/http";
import {ErrorDisplayComponent} from './components/error-display/error-display.component';
import {ErrorInterceptor} from "./interceptors/error.interceptor";
import {ToastrModule} from "ngx-toastr";
import {MaterialModule} from "./material/material.module";
import {HeaderComponent} from './components/header/header.component';
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {LanguageService} from "./services/language/language.service";
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {APP_BASE_HREF} from '@angular/common';
import {ErrorService} from "./services/error/error.service";

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http, 'assets/i18n/', '.json');
}

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
    TranslateModule.forRoot({
      defaultLanguage: LanguageService.defaultLanguage,
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    ToastrModule.forRoot({
      timeOut: 5000,
      preventDuplicates: true
    })
  ],
  providers: [
    ErrorService,
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true, deps: [ErrorService]},
    {provide: APP_BASE_HREF, useValue: '/app'}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
