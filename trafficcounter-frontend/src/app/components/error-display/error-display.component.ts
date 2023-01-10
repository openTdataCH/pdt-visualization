/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {Component, OnInit} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {ErrorService} from "../../services/error/error.service";

/**
 * Component responsible for the display of errors.
 * Errors are consumed from the error service and displayed over Toastr.
 */
@Component({
  selector: 'app-error-display',
  templateUrl: './error-display.component.html',
  styleUrls: ['./error-display.component.scss']
})
export class ErrorDisplayComponent implements OnInit {

  constructor(
    private readonly errorService: ErrorService,
    private readonly toastr: ToastrService
  ) {
  }

  ngOnInit(): void {
    this.errorService.error$.subscribe(error => {
      if (error != null) {
        this.toastr.error(error.message, 'An error occurred');
      }
    });
  }

}
