package com.example.lyudvigv.ffuel.DataModel;

/**
 * Created by LyudvigV on 2/1/2018.
 */

public  class AwbProductDataRequest {

    public String ServiceAction;
    public Integer ParentID;

    public Condition Condition;

    public Integer ID;
    public Integer PageIndex;
    public Integer Offset;
    public Integer PageSize;
    //: 0, "PageIndex" : 0, "Offset" : 0, "PageSize" : 0
    public class Condition {

        public Boolean DataHub;
        public Integer CustomerID;
        public Integer CustomerWebUserID;

    }
}
