import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {catchError, Observable, ObservableInput, of, retry} from 'rxjs';
import {environment} from "../../environments/environment";
import {ErrorService} from "../services/error/error.service";

/**
 * Interceptor for error handling.
 * If a connection fails, the request is sent again
 * (number of retries is defined in the environment configuration).
 * After the defined number of retries, the error is caught by sending the error to the error service.
 */
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private readonly errorService: ErrorService) {}

  /**
   * Handles an error thrown during a request.
   * @param error the thrown error
   */
  public handleError(error: any): ObservableInput<any> {
    this.errorService.setError(error);
    return of([]);
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request)
      .pipe(
        retry(environment.api.retries),
        catchError(this.handleError)
      );
  }
}
