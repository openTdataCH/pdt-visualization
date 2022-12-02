import {Inject, Injectable, NgZone, Optional} from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpResponse} from '@angular/common/http';

import {Observable} from 'rxjs';
import {Configuration} from "../configuration";
import {BASE_PATH} from "../variables";
import {GeoJsonFeatureCollectionDto} from "../models/geo-json-feature-collection-dto";
import {environment} from "../../../environments/environment";
import {ApiModule} from "../api.module";
import {ReactiveSseService} from "./reactive-sse-service";

@Injectable({
    providedIn: ApiModule
})
export class VehicleDataControllerService extends ReactiveSseService {

    protected basePath = environment.api.basePath;
    public defaultHeaders = new HttpHeaders();
    public configuration = new Configuration();

    constructor(zone: NgZone, protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {
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


    /**
     *
     *
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getCurrentVehicleData(observe?: 'body', reportProgress?: boolean): Observable<GeoJsonFeatureCollectionDto>;
    public getCurrentVehicleData(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<GeoJsonFeatureCollectionDto>>;
    public getCurrentVehicleData(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<GeoJsonFeatureCollectionDto>>;
    public getCurrentVehicleData(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

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

        return this.httpClient.request<GeoJsonFeatureCollectionDto>('get',`${this.basePath}/vehicledata`,
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
    public getCurrentVehicleDataReactive(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

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
        const consumes: string[] = [
        ];

        const url = `${this.basePath}/vehicledata/live`;

        return this.createEventStream(url);
    }

}
