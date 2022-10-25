import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  private readonly errorSubject$: BehaviorSubject<HttpErrorResponse|null> = new BehaviorSubject<HttpErrorResponse|null>(null)

  public get error$(): Observable<HttpErrorResponse|null> {
    return this.errorSubject$;
  }

  constructor() { }

  public setError(error: HttpErrorResponse) {
    this.errorSubject$.next(error);
  }
}
