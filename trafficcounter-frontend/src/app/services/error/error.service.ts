/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

/**
 * Handler for HTTP errors.
 */
@Injectable()
export class ErrorService {

  private readonly errorSubject$: BehaviorSubject<HttpErrorResponse | null> = new BehaviorSubject<HttpErrorResponse | null>(null)

  constructor() {
  }

  /**
   * The most recent error or null.
   */
  public get error$(): Observable<HttpErrorResponse | null> {
    return this.errorSubject$;
  }

  /**
   * Sets a new error.
   * @param error new error
   */
  public setError(error: HttpErrorResponse) {
    this.errorSubject$.next(error);
  }
}
