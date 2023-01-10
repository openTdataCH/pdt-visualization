/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {Inject, Injectable, NgZone, Optional} from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';

import {Observable} from 'rxjs';
import {Configuration} from "../configuration";
import {BASE_PATH} from "../variables";
import {GeoJsonFeatureCollectionDto} from "../models/geo-json-feature-collection-dto";
import {environment} from "../../../environments/environment";
import {ApiModule} from "../api.module";
import {ReactiveSseService} from "./reactive-sse-service";
import {HistoricDataCollectionDto} from "../models/historic-data-collection-dto";
import {CustomHttpUrlEncodingCodec} from "../encoder";

@Injectable({
  providedIn: ApiModule
})
export class VehicleDataControllerService extends ReactiveSseService {

  public defaultHeaders = new HttpHeaders();
  public configuration = new Configuration();
  protected basePath = environment.api.basePath;

  constructor(zone: NgZone, protected httpClient: HttpClient, @Optional() @Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {
    super(zone);
    if (basePath) {
      this.basePath = basePath;
    }
    if (configuration) {
      this.configuration = configuration;
      this.basePath = basePath || configuration.basePath || this.basePath;
    }
  }

  /**
   *
   *
   * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
   * @param reportProgress flag to report request and response progress.
   */
  public getCurrentVehicleData(observe?: 'body', reportProgress?: boolean): Observable<GeoJsonFeatureCollectionDto>;

  public getCurrentVehicleData(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<GeoJsonFeatureCollectionDto>>;

  public getCurrentVehicleData(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<GeoJsonFeatureCollectionDto>>;

  public getCurrentVehicleData(observe: any = 'body', reportProgress: boolean = false): Observable<any> {

    let headers = this.defaultHeaders;

    // to determine the Accept header
    let httpHeaderAccepts: string[] = [
      '*/*'
    ];
    const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
    if (httpHeaderAcceptSelected != undefined) {
      headers = headers.set('Accept', httpHeaderAcceptSelected);
    }

    // to determine the Content-Type header
    const consumes: string[] = [];

    return this.httpClient.request<GeoJsonFeatureCollectionDto>('get', `${this.basePath}/vehicledata`,
      {
        withCredentials: this.configuration.withCredentials,
        headers: headers,
        observe: observe,
        reportProgress: reportProgress
      }
    );
  }

  /**
   *
   *
   * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
   * @param reportProgress flag to report request and response progress.
   */
  public getCurrentVehicleDataReactive(observe?: 'body', reportProgress?: boolean): Observable<GeoJsonFeatureCollectionDto>;

  public getCurrentVehicleDataReactive(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<GeoJsonFeatureCollectionDto>>;

  public getCurrentVehicleDataReactive(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<GeoJsonFeatureCollectionDto>>;

  public getCurrentVehicleDataReactive(observe: any = 'body', reportProgress: boolean = false): Observable<any> {

    let headers = this.defaultHeaders;

    // to determine the Accept header
    let httpHeaderAccepts: string[] = [
      'text/event-stream'
    ];
    const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
    if (httpHeaderAcceptSelected != undefined) {
      headers = headers.set('Accept', httpHeaderAcceptSelected);
    }

    // to determine the Content-Type header
    const consumes: string[] = [];

    const url = `${this.basePath}/vehicledata/stream-flux`;

    return this.createEventStream(url);
  }

  /**
   *
   *
   * @param id
   * @param duration
   * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
   * @param reportProgress flag to report request and response progress.
   */
  public getHistoricalVehicleData(id: string, duration: string, observe?: 'body', reportProgress?: boolean): Observable<HistoricDataCollectionDto>;

  public getHistoricalVehicleData(id: string, duration: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<HistoricDataCollectionDto>>;

  public getHistoricalVehicleData(id: string, duration: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<HistoricDataCollectionDto>>;

  public getHistoricalVehicleData(id: string, duration: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

      if (id === null || id === undefined) {
          throw new Error('Required parameter id was null or undefined when calling getHistoricalVehicleData.');
      }

      if (duration === null || duration === undefined) {
          throw new Error('Required parameter duration was null or undefined when calling getHistoricalVehicleData.');
      }

      let queryParameters = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
      if (duration !== undefined && duration !== null) {
          queryParameters = queryParameters.set('duration', <any>duration);
      }

      let headers = this.defaultHeaders;

      // to determine the Accept header
      let httpHeaderAccepts: string[] = [
          '*/*'
      ];
      const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
      if (httpHeaderAcceptSelected != undefined) {
          headers = headers.set('Accept', httpHeaderAcceptSelected);
      }

      // to determine the Content-Type header
      const consumes: string[] = [
      ];

      return this.httpClient.request<HistoricDataCollectionDto>('get',`${this.basePath}/vehicledata/history/${encodeURIComponent(String(id))}`,
          {
              params: queryParameters,
              withCredentials: this.configuration.withCredentials,
              headers: headers,
              observe: observe,
              reportProgress: reportProgress
          }
      );
  }

  /**
   * @param consumes string[] mime-types
   * @return true: consumes contains 'multipart/form-data', false: otherwise
   */
  private canConsumeForm(consumes: string[]): boolean {
    const form = 'multipart/form-data';
    for (const consume of consumes) {
      if (form === consume) {
        return true;
      }
    }
    return false;
  }

}
