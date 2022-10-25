import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {catchError, Observable, ObservableInput, of, retry} from 'rxjs';
import {environment} from "../../environments/environment";
import {ErrorService} from "../services/error/error.service";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private readonly errorService: ErrorService) {}

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
