import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

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
  showMenu$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true)

  constructor() { }
}
