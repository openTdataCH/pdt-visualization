import {Injectable} from '@angular/core';
import {Observable, of} from "rxjs";
import {MeasurementPointDto} from "../../models/api/measurement-point-dto.model";

@Injectable({
  providedIn: 'root'
})
export class MeasurementPointService {

  constructor() { }

  public getAllMeasurementPoints(): Observable<Array<MeasurementPointDto>> {
    //TODO use real data instead of random generated mock data
    const measurementPoints: MeasurementPointDto[] = [];
    for (let i = 0; i < 2500; i++) {
      measurementPoints.push({
        id: "Point-" + Math.random(),
        longitude: Math.random() + 8.1336,
        latitude: Math.random() + 46.784,
      })
    }
    return of(measurementPoints);
  }
}
