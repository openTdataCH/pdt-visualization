import {Inject, Injectable, Optional} from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpResponse} from '@angular/common/http';

import {Observable} from 'rxjs';
import {Configuration} from "../configuration";
import {BASE_PATH} from "../variables";
import {GeoJsonFeatureCollectionDto} from "../models/geo-json-feature-collection-dto";
import {ApiModule} from "../api.module";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: ApiModule
})
export class MeasurementPointControllerService {

    protected basePath = environment.apiBasePath;
    public defaultHeaders = new HttpHeaders();
    public configuration = new Configuration();

    constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {
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
    public getMeasurementPointsGeoJson(observe?: 'body', reportProgress?: boolean): Observable<GeoJsonFeatureCollectionDto>;
    public getMeasurementPointsGeoJson(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<GeoJsonFeatureCollectionDto>>;
    public getMeasurementPointsGeoJson(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<GeoJsonFeatureCollectionDto>>;
    public getMeasurementPointsGeoJson(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

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

        return this.httpClient.request<GeoJsonFeatureCollectionDto>('get',`${this.basePath}/measurementpoints`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}
