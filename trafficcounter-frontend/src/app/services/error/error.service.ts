import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

/**
 * Handler for HTTP errors.
 */
@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  private readonly errorSubject$: BehaviorSubject<HttpErrorResponse|null> = new BehaviorSubject<HttpErrorResponse|null>(null)

  /**
   * The most recent error or null.
   */
  public get error$(): Observable<HttpErrorResponse|null> {
    return this.errorSubject$;
  }

  constructor() { }

  /**
   * Sets a new error.
   * @param error new error
   */
  public setError(error: HttpErrorResponse) {
    this.errorSubject$.next(error);
  }
}
