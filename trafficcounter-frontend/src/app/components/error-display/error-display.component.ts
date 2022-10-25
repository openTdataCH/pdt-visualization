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
  ) { }

  ngOnInit(): void {
    this.errorService.error$.subscribe(error => {
      this.toastr.error(error?.message, 'An error occurred');
    });
  }

}
