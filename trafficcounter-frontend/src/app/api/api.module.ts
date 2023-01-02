/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {ModuleWithProviders, NgModule, Optional, SkipSelf} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Configuration} from "./configuration";
import {HttpClient} from "@angular/common/http";


@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class ApiModule {
  constructor(@Optional() @SkipSelf() parentModule: ApiModule,
              @Optional() http: HttpClient) {
    if (parentModule) {
      throw new Error('ApiModule is already loaded. Import in your base AppModule only.');
    }
    if (!http) {
      throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
        'See also https://github.com/angular/angular/issues/20575');
    }
  }

  public static forRoot(configurationFactory: () => Configuration): ModuleWithProviders<ApiModule> {
    return {
      ngModule: ApiModule,
      providers: [{provide: Configuration, useFactory: configurationFactory}]
    };
  }
}
