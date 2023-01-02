/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {Observable} from "rxjs";
import {NgZone} from "@angular/core";

/**
 * An abstract representation of a service delivering reactive data over SSE.
 */
export abstract class ReactiveSseService {

  protected constructor(protected zone: NgZone) {
  }

  /**
   * Creates a new event stream by consuming an SSE endpoint.
   * @param url URL of SSE endpoint
   */
  protected createEventStream<T>(url: string): Observable<T> {

    return new Observable(observer => {
      const eventSource = new EventSource(url);
      eventSource.onopen = event => {
        console.log('opened connection');
      }
      eventSource.onmessage = event => {
        this.zone.run(() => {
          const data = JSON.parse(event.data);
          observer.next(data);
        });
      };
      eventSource.onerror = error => {
        this.zone.run(() => {
          observer.error(error);
        });
      };
    });
  }
}
