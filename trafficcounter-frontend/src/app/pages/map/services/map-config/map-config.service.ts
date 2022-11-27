import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {MapMode} from "../../models/map-mode";

/**
 * Service for map configuration.
 */
@Injectable({
  providedIn: 'root'
})
export class MapConfigService {

  /**
   * State of the menu display.
   */
  showMenu$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);

  /**
   * Mode of the map.
   */
  mapMode$: BehaviorSubject<MapMode> = new BehaviorSubject<MapMode>(MapMode.MeasurementPoints);

  constructor() { }
}
