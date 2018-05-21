package com.example.lyudvigv.ffuel.DataModel;

/**
 * Created by LyudvigV on 2/1/2018.
 */

public  class ShipmentDataRequest {

    public String ServiceAction;

    public Condition Condition;

    public class Condition {
        public String CreateDate;
        public Boolean DataHub;
        public Boolean FlightCacheData;
    }
}
