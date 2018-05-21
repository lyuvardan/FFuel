package com.example.lyudvigv.ffuel.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lyudvigv.ffuel.DataModel.ShipmentData;
import com.example.lyudvigv.ffuel.R;
import com.example.lyudvigv.ffuel.main_tab.fragments.Shipment.ShipmentDetailActivity;
import com.google.gson.Gson;

/**
 * Created by LyudvigV on 12/19/2017.
 */

public class BottomShipmentAdapter extends ArrayAdapter<ShipmentData> {
    private int _layoutResourceId;
    private ShipmentData _shipmentData;
    private Context _context;
    private LinearLayout _llLeftColorLayout;

    private TextView _awb;
    private TextView _airlineCode;
    private TextView _tvConsigneeName;
    private TextView _tvDepartureTime;
    private TextView _tvArrivalTime;
    private TextView _status;

    private LinearLayout _llStatBarWithNoConFlight;
    private TextView _tvNoConn1;
    private TextView _tvNoConn2;

    private LinearLayout _llStatBarWithOneConFlight;
    private TextView _tvOneConn1;
    private TextView _tvOneConn2;
    private TextView _tvOneConn3;

    private LinearLayout _llStatBarWithTwoConFlight;
    private TextView _tvTwoConn1;
    private TextView _tvTwoConn2;
    private TextView _tvTwoConn3;
    private TextView _tvTwoConn4;

    public BottomShipmentAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        _layoutResourceId=layoutResourceId;
        _context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        _shipmentData = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.shipment_bottom_list_item, null);
        _llLeftColorLayout = (LinearLayout)convertView.findViewById(R.id.llLeftColorLayout);

        LayerDrawable d = (LayerDrawable) _llLeftColorLayout.getBackground();
        GradientDrawable shape = (GradientDrawable)d.findDrawableByLayerId(R.id.shipmentBackground);

        shape.setStroke(3,Color.parseColor("#e0e0e0"));


        _awb = (TextView)convertView.findViewById(R.id.awb);
        _awb.setText(_shipmentData.AWBCode + "-"+_shipmentData.AirWayBillNo);
        _airlineCode = (TextView)convertView.findViewById(R.id.airlineCode);
        _airlineCode.setText(_shipmentData.AirlineCode);

        _tvConsigneeName = (TextView)convertView.findViewById(R.id.tvConsigneeName);
        _tvConsigneeName.setText(_shipmentData.ConsigneeName);

        _tvDepartureTime=(TextView)convertView.findViewById(R.id.tvDepartureTime);
        _tvDepartureTime.setText(_shipmentData.FlightTime);

        _tvArrivalTime=(TextView)convertView.findViewById(R.id.tvArrivalTime);
        _tvArrivalTime.setText(_shipmentData.ArrivalTime); //TODO
        _status = (TextView)convertView.findViewById(R.id.status);

        if(_shipmentData.WebStatus.equals("Shipped"))
        {
            _shipmentData.StatusColor = "#82c268";
            shape.setColor(Color.parseColor(_shipmentData.StatusColor));
            _status.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
        }
        if(_shipmentData.WebStatus.equals("Loaded"))
        {
            _shipmentData.StatusColor = "#ffc63c";
            shape.setColor(Color.parseColor(_shipmentData.StatusColor));
            _status.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
        }
        if(_shipmentData.WebStatus.equals("Arrived"))
        {
            _shipmentData.StatusColor = "#3ca2ff";
            shape.setColor(Color.parseColor(_shipmentData.StatusColor));
            _status.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
        }
        if(_shipmentData.WebStatus.equals("Transit"))
        {
            _shipmentData.StatusColor = "#3ca2ff";
            shape.setColor(Color.parseColor(_shipmentData.StatusColor));
            _status.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
        }
        if(_shipmentData.WebStatus.equals("In the Air"))
        {
            _shipmentData.StatusColor = "#11ffff";
            shape.setColor(Color.parseColor(_shipmentData.StatusColor));
            _status.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
        }
        _status.setText(_shipmentData.WebStatus);

        _llStatBarWithNoConFlight = (LinearLayout)convertView.findViewById(R.id.llStatBarWithNoConFlight);
        _tvNoConn1 = (TextView)convertView.findViewById(R.id.tvNoConn1);
        _tvNoConn2 = (TextView)convertView.findViewById(R.id.tvNoConn2);
        _llStatBarWithNoConFlight.setVisibility(View.GONE);

        _llStatBarWithOneConFlight = (LinearLayout)convertView.findViewById(R.id.llStatBarWithOneConFlight);
        _tvOneConn1 = (TextView)convertView.findViewById(R.id.tvOneConn1);
        _tvOneConn2 = (TextView)convertView.findViewById(R.id.tvOneConn2);
        _tvOneConn3 = (TextView)convertView.findViewById(R.id.tvOneConn3);
        _llStatBarWithOneConFlight.setVisibility(View.GONE);

        _llStatBarWithTwoConFlight = (LinearLayout)convertView.findViewById(R.id.llStatBarWithTwoConFlight);
        _tvTwoConn1 = (TextView)convertView.findViewById(R.id.tvTwoConn1);
        _tvTwoConn2 = (TextView)convertView.findViewById(R.id.tvTwoConn2);
        _tvTwoConn3 = (TextView)convertView.findViewById(R.id.tvTwoConn3);
        _tvTwoConn4 = (TextView)convertView.findViewById(R.id.tvTwoConn4);
        _llStatBarWithTwoConFlight.setVisibility(View.GONE);

        if(_shipmentData.FlightCount==3)
        {
            _llStatBarWithTwoConFlight.setVisibility(View.VISIBLE);
            _llStatBarWithNoConFlight.setVisibility(View.GONE);
            _llStatBarWithOneConFlight.setVisibility(View.GONE);

            _tvTwoConn1.setText(_shipmentData.DepartureAirportCode);
            _tvTwoConn2.setText(_shipmentData.AirportCode_2);
            _tvTwoConn3.setText(_shipmentData.AirportCode_3);
            _tvTwoConn4.setText(_shipmentData.DestinationAirportCode);
        }else if(_shipmentData.FlightCount==2)
        {
            _llStatBarWithTwoConFlight.setVisibility(View.GONE);
            _llStatBarWithOneConFlight.setVisibility(View.VISIBLE);
            _llStatBarWithNoConFlight.setVisibility(View.GONE);
            _tvOneConn1.setText(_shipmentData.DepartureAirportCode);
            _tvOneConn2.setText(_shipmentData.AirportCode_2);
            _tvOneConn3.setText(_shipmentData.DestinationAirportCode);
        }else{
            _llStatBarWithTwoConFlight.setVisibility(View.GONE);
            _llStatBarWithOneConFlight.setVisibility(View.GONE);
            _llStatBarWithNoConFlight.setVisibility(View.VISIBLE);
            _tvNoConn1.setText(_shipmentData.DepartureAirportCode);
            _tvNoConn2.setText(_shipmentData.DestinationAirportCode);
        }

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, ShipmentDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                final Gson gson = new Gson();
                String jsonExtraObj = gson.toJson(getItem(position)).toString();
                intent.putExtra("shipment", jsonExtraObj);
                _context.startActivity(intent);
            }
        });
        return convertView;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
