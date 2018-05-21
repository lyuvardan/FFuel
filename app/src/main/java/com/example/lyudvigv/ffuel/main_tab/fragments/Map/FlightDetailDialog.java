package com.example.lyudvigv.ffuel.main_tab.fragments.Map;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lyudvigv.ffuel.DataModel.FlightAwareData;
import com.example.lyudvigv.ffuel.DataModel.ShipmentData;
import com.example.lyudvigv.ffuel.R;
import com.example.lyudvigv.ffuel.loginAndRegistration.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by LyudvigV on 1/24/2018.
 */

public class FlightDetailDialog extends Dialog {
    private UserInfo _userInfo;
    private Context _context;
    private SimpleDateFormat _tzFormatter;
    private SimpleDateFormat _formatter;

    private FlightAwareData _flightAwareData;
    private List<ShipmentData> _shData;
    private TextView _tvAirCompany;
    private TextView _tvFlightStatus;
    private TextView _tvAirwayBillNo;

    private TextView _tvDepartureTime;
    private TextView _tvDepartureDelay;
    private Date _faDepTime;

    private TextView _tvArrivalTime;
    private TextView _tvArrivalDelay;
    private Date _faArrTime;

    private View _vPassedWay;
    private View _vRemainedWay;

    private TextView _tvOrigin;
    private TextView _tvDest;

    private LinearLayout _awbContainerLayout;

    public FlightDetailDialog(Context context, FlightAwareData data, List<ShipmentData> shData)
    {
        super(context);
        _context = context;
        _flightAwareData = data;
        _shData = shData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_detail_dialog_layout);
        initialize();
        loadData();
    }

    private void initialize(){
        _tvFlightStatus = (TextView) this.findViewById(R.id.tvFlightStatus);
        //_tvAirwayBillNo = (TextView) this.findViewById(R.id.tvAirwayBillNo);
        _tvAirCompany = (TextView) this.findViewById(R.id.tvAirCompany);

        _tvDepartureTime = (TextView) this.findViewById(R.id.tvDepartureTime);
        _tvDepartureDelay = (TextView) this.findViewById(R.id.tvDepDelay);
        _tzFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        _formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        _tvArrivalTime = (TextView) this.findViewById(R.id.tvArrTime);
        _tvArrivalDelay = (TextView) this.findViewById(R.id.tvArrDelay);

        _vPassedWay = (View)this.findViewById(R.id.passedWay);
        _vRemainedWay = (View)this.findViewById(R.id.remainedWay);

        _tvOrigin = (TextView)this.findViewById(R.id.tvOrigin);
        _tvDest = (TextView)this.findViewById(R.id.tvDest);

        _awbContainerLayout = (LinearLayout)this.findViewById(R.id.awbContainerLayout);
    }

    private void loadData(){


        _tvFlightStatus.setText(_flightAwareData.WebStatus);
        _tvAirCompany.setText(_shData.get(0).AirlineCode);
        try {
            _faDepTime = _tzFormatter.parse(_flightAwareData.DepeartueTime);
            _faArrTime = _tzFormatter.parse(_flightAwareData.EstimatedArrivalTime);
            int totalDifTimeInMin =(int)(_faArrTime.getTime()-_faDepTime.getTime())/(1000*60);
            int totalDifRemainedTimeInMin = totalDifTimeInMin - totalDifTimeInMin*(int)Double.parseDouble(_flightAwareData.PassedTimePercent)/100;
        }catch (Exception e){}

        String faDepTime = _formatter.format(_faDepTime);
        _tvDepartureTime.setText(faDepTime);
        if(Integer.parseInt(_flightAwareData.DepartueTimeDiff)/60>0) {
            _tvDepartureDelay.setText(Integer.parseInt(_flightAwareData.DepartueTimeDiff) / 60 + " m late");
        }
        else {
            _tvDepartureDelay.setText(Math.abs(Integer.parseInt(_flightAwareData.DepartueTimeDiff) / 60) + " m early");
            _tvDepartureDelay.setTextColor(Color.GREEN);
        }

        String faArrTime = _formatter.format(_faArrTime);
        _tvArrivalTime.setText(faArrTime);
        _vPassedWay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5,(100-(int)Double.parseDouble(_flightAwareData.PassedTimePercent))/5));
        _vRemainedWay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1,((int)Double.parseDouble(_flightAwareData.PassedTimePercent))/5));
        //_tvArrivalDelay.setText();
        _tvOrigin.setText(_flightAwareData.Origin);
        _tvDest.setText(_flightAwareData.Destination);

        for (int i=0;i<_shData.size();i++){
            View v = this.getLayoutInflater().inflate(R.layout.flight_detail_dialog_awb_layout, _awbContainerLayout);

            TextView awb = (TextView) v.findViewWithTag("awbNumber");
            awb.setTag("awbNumber"+i);
            awb.setText(_shData.get(i).AWBCode+"-"+_shData.get(i).AirWayBillNo);

            TextView poNumber = (TextView) v.findViewWithTag("poNumber");
            poNumber.setTag("poNumber"+i);
            if(_shData.get(i).ShipperPONo!=null) {
                poNumber.setText(_shData.get(i).ShipperPONo);
            }

            TextView consignee = (TextView) v.findViewWithTag("consignee");
            consignee.setTag("consignee"+i);
            if(_shData.get(i).ConsigneeName!=null) {
                consignee.setText(_shData.get(i).ConsigneeName);
            }
        }
    }
}
