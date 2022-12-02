package ch.bfh.trafficcounter.event;

/**
 * Represents an update event.
 * An update event occurs, if data is updated.
 *
 * @author Manuel Riesen
 */
public enum UpdateEvent {

     /**
      * Vehicle amount update event.
      */
     VEHICLE_AMOUNT,
     /**
     * Speed data update event.
     */
    SPEED_DATA,
    /**
     * Event for all dynamic data
     */
    ALL
}
