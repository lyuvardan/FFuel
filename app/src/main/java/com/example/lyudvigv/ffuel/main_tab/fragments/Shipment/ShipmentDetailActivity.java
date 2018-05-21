package com.example.lyudvigv.ffuel.main_tab.fragments.Shipment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lyudvigv.ffuel.DataModel.AwbProductData;
import com.example.lyudvigv.ffuel.DataModel.AwbProductDataRequest;
import com.example.lyudvigv.ffuel.DataModel.FreightShipmentData;
import com.example.lyudvigv.ffuel.DataModel.ShipmentData;
import com.example.lyudvigv.ffuel.MyApplication;
import com.example.lyudvigv.ffuel.R;
import com.example.lyudvigv.ffuel.RequestManager;
import com.example.lyudvigv.ffuel.loginAndRegistration.UserInfo;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LyudvigV on 12/21/2017.
 */

public class ShipmentDetailActivity extends AppCompatActivity {

    private UserInfo _userInfo;
    private String _awbProductsUrl;
    private ShipmentDetailActivity _activity;
    private ShipmentData _shipmentData;
    private Toolbar _toolbar;
    private ImageView _backIcon;
    private TextView _shipDetTlbrText;

    private TextView _tvAwb;
    private TextView _tvStatus;
    private TextView _tvAirlineCode;
    private TextView _tvConsignee;

    private TextView _departureTime;
    private TextView _arrivalTime;

    private LinearLayout _llAwbContainer;

    private TextView _stReceived;
    private TextView _stLoaded;
    private TextView _stShipped;
    private TextView _stTransit;
    private TextView _stArrived;

    private AwbProductData[] _awbProducts;
    private LinearLayout _llAwbProductContainer;
    private LinearLayout _llAwbChargeContainerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.shipment_detail);

        getDataFromIntent();

        initializeNavigationComponents();

        initialize();

        loadData();
    }

    private void getDataFromIntent(){
        Intent i = getIntent();
        String shipmentJson = i.getStringExtra("shipment");
        ShipmentData shipmentData = new Gson().fromJson(shipmentJson, ShipmentData.class);
        _shipmentData = shipmentData;
    }

    private void initializeNavigationComponents()
    {
        _toolbar = (Toolbar) findViewById(R.id.app_bar_shipment_detail);
        _backIcon = (ImageView)findViewById(R.id.backIcon);
        //_shipDetTlbrText = (TextView)findViewById(R.id.shipDetTlbrText);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        _activity=this;
        _backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _activity.finish();
            }
        });
    }

    private void initialize(){
        _userInfo = ((MyApplication)this.getApplication()).getUserInfo();
        _awbProductsUrl = getResources().getString(R.string.service_url)+ "api/json/data/list";
        _tvAwb = (TextView)findViewById(R.id.tvAwb);
        _tvAirlineCode = (TextView)findViewById(R.id.tvAirlineCode);
        _tvConsignee = (TextView)findViewById(R.id.tvConsigneeName);
        _departureTime = (TextView)findViewById(R.id.departureTime);
        _arrivalTime = (TextView)findViewById(R.id.arrivalTime);
        //_tvStatus = (TextView)findViewById(R.id.tvStatus);
        _llAwbContainer = (LinearLayout)findViewById(R.id.llAwbContainerLayout);
        _stReceived = (TextView)findViewById(R.id.stReceived);
        _stLoaded = (TextView)findViewById(R.id.stLoaded);
        _stShipped = (TextView)findViewById(R.id.stShipped);
        _stTransit = (TextView)findViewById(R.id.stTransit);
        _stArrived = (TextView)findViewById(R.id.stArrived);
        _llAwbProductContainer = (LinearLayout)findViewById(R.id.llAwbProductContainerLayout);
        Thread awbProductThread = new Thread(new Runnable() {
            @Override
            public void run() {
                getAwbProducts(_awbProductsUrl);
            }
        });
        awbProductThread.start();
        _llAwbChargeContainerLayout = (LinearLayout)findViewById(R.id.llAwbChargeContainerLayout);
        fillAWBCharges(_shipmentData.AirOrderChargesWms);
    }

    private void loadData(){
        _tvAwb.setText(_shipmentData.AWBCode+ "-"+_shipmentData.AirWayBillNo);
        _tvAirlineCode.setText(_shipmentData.AirlineCode);
        _tvConsignee.setText(_shipmentData.ConsigneeName);
        _departureTime.setText(_shipmentData.FlightTime);
        _arrivalTime.setText(_shipmentData.ArrivalTime);
        String[] airlines = null;
        String[] flights = null;
        String[] origins = null;
        String[] destinations = null;
        String[] schDepartures = null;
        String[] actDepartures = null;
        String[] schArrivals = null;
        String[] actArrivals = null;
        if(_shipmentData.AirlineName_3!=null && _shipmentData.FlightCount==3) {
            //_connectedFlightCount = 3;
            airlines= new String[_shipmentData.FlightCount];
            airlines[0] = _shipmentData.AirlineName;
            airlines[1] = _shipmentData.AirlineName_2;
            airlines[2] = _shipmentData.AirlineName_3;

            flights = new String[_shipmentData.FlightCount];
            flights[0]=_shipmentData.FlightNo;
            flights[1]=_shipmentData.FlightNo_2;
            flights[2]=_shipmentData.FlightNo_3;

            origins = new String[_shipmentData.FlightCount];
            origins[0] = _shipmentData.DepartureAirportCode;
            origins[1]=_shipmentData.AirportCode_2;
            origins[2]=_shipmentData.AirportCode_3;

            destinations = new String[_shipmentData.FlightCount];
            destinations[0] = _shipmentData.AirportCode_2;
            destinations[1] = _shipmentData.AirportCode_3;
            destinations[2] = _shipmentData.DestinationAirportCode;

            schDepartures = new String[_shipmentData.FlightCount];
            schDepartures[0] = _shipmentData.Flight1DepartureScheduled;
            schDepartures[1] = _shipmentData.Flight2DepartureScheduled;
            schDepartures[1] = _shipmentData.Flight3DepartureScheduled;

            actDepartures = new String[_shipmentData.FlightCount];
            actDepartures[0] = _shipmentData.Flight1DepartureActual;
            actDepartures[1] = _shipmentData.Flight2DepartureActual;
            actDepartures[2] = _shipmentData.Flight3DepartureActual;

            schArrivals = new String[_shipmentData.FlightCount];
            schArrivals[0] = _shipmentData.Flight1ArrivalScheduled;
            schArrivals[1] = _shipmentData.Flight2ArrivalScheduled;
            schArrivals[2] = _shipmentData.Flight3ArrivalScheduled;

            actArrivals = new String[_shipmentData.FlightCount];
            actArrivals[0] = _shipmentData.Flight1ArrivalActual;
            actArrivals[1] = _shipmentData.Flight2ArrivalActual;
            actArrivals[2] = _shipmentData.Flight3ArrivalActual;

        }else if(_shipmentData.AirlineName_2!=null && _shipmentData.FlightCount==2){
            //_connectedFlightCount=2;
            airlines= new String[_shipmentData.FlightCount];
            airlines[0] = _shipmentData.AirlineName;
            airlines[1] = _shipmentData.AirlineName_2;

            flights = new String[_shipmentData.FlightCount];
            flights[0]=_shipmentData.FlightNo;
            flights[1]=_shipmentData.FlightNo_2;

            origins = new String[_shipmentData.FlightCount];
            origins[0] = _shipmentData.DepartureAirportCode;
            origins[1]=_shipmentData.AirportCode_2;

            destinations = new String[_shipmentData.FlightCount];
            destinations[0] = _shipmentData.AirportCode_2;
            destinations[1] = _shipmentData.DestinationAirportCode;

            schDepartures = new String[_shipmentData.FlightCount];
            schDepartures[0] = _shipmentData.Flight1DepartureScheduled;
            schDepartures[1] = _shipmentData.Flight2DepartureScheduled;

            actDepartures = new String[_shipmentData.FlightCount];
            actDepartures[0] = _shipmentData.Flight1DepartureActual;
            actDepartures[1] = _shipmentData.Flight2DepartureActual;

            schArrivals = new String[_shipmentData.FlightCount];
            schArrivals[0] = _shipmentData.Flight1ArrivalScheduled;
            schArrivals[1] = _shipmentData.Flight2ArrivalScheduled;

            actArrivals = new String[_shipmentData.FlightCount];
            actArrivals[0] = _shipmentData.Flight1ArrivalActual;
            actArrivals[1] = _shipmentData.Flight2ArrivalActual;
        }else {
            //_connectedFlightCount=1;
            if(_shipmentData.FlightCount==0){
                _shipmentData.FlightCount=1;
            }
            airlines= new String[_shipmentData.FlightCount];
            airlines[0] = _shipmentData.AirlineName;

            flights = new String[_shipmentData.FlightCount];
            flights[0]=_shipmentData.FlightNo;

            origins = new String[_shipmentData.FlightCount];
            origins[0] = _shipmentData.DepartureAirportCode;

            destinations = new String[_shipmentData.FlightCount];
            destinations[0] = _shipmentData.DestinationAirportCode;

            schDepartures = new String[_shipmentData.FlightCount];
            schDepartures[0] = _shipmentData.Flight1DepartureScheduled;

            actDepartures = new String[_shipmentData.FlightCount];
            actDepartures[0] = _shipmentData.Flight1DepartureActual;

            schArrivals = new String[_shipmentData.FlightCount];
            schArrivals[0] = _shipmentData.Flight1ArrivalScheduled;

            actArrivals = new String[_shipmentData.FlightCount];
            actArrivals[0] = _shipmentData.Flight1ArrivalActual;
        }

        for(int i =0;i<_shipmentData.FlightCount;i++){

            View v = this.getLayoutInflater().inflate(R.layout.shipment_detail_awb_block, _llAwbContainer);
            LinearLayout llShipmentDetAWBBlock = (LinearLayout)v.findViewWithTag("llShipmentDetAWBBlock");
            llShipmentDetAWBBlock.setTag("llShipmentDetAWBBlock"+i);

            if((i%2)==0){llShipmentDetAWBBlock.setBackgroundColor(Color.parseColor("#ffffff"));}else{llShipmentDetAWBBlock.setBackgroundColor(Color.parseColor("#f7f7f7"));}

            TextView flightDetail = (TextView)v.findViewWithTag("flightDetail");
            flightDetail.setTag("flightDetail"+i);

            if(i==0){ flightDetail.setText("origin");}else{ flightDetail.setText("connected");}

            TextView airline = (TextView)v.findViewWithTag("airline");
            airline.setTag("airline"+i);
            airline.setText(airlines[i]);

            TextView flight = (TextView)v.findViewWithTag("flight");
            flight.setTag("flight"+i);
            flight.setText(flights[i]);

            TextView origin = (TextView)v.findViewWithTag("origin");
            origin.setTag("origin"+i);
            origin.setText(origins[i]);

            TextView destination = (TextView)v.findViewWithTag("destination");
            destination.setTag("destination"+i);
            destination.setText(destinations[i]);

            TextView depScheduled = (TextView)v.findViewWithTag("depScheduled");
            depScheduled.setTag("depScheduled"+i);
            depScheduled.setText(schDepartures[i]);

            TextView depActual = (TextView)v.findViewWithTag("depActual");
            depActual.setTag("depActual"+i);
            depActual.setText(actDepartures[i]);

            TextView arrScheduled = (TextView)v.findViewWithTag("arrScheduled");
            arrScheduled.setTag("arrScheduled"+i);
            arrScheduled.setText(schArrivals[i]);

            TextView arrActual = (TextView)v.findViewWithTag("arrActual");
            arrActual.setTag("arrActual"+i);
            arrActual.setText(actArrivals[i]);
        }

        if(_shipmentData.WebStatus.equals("Received")){
            _stReceived.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stReceived.setTextColor(Color.parseColor("#ffffff"));
        }
        if(_shipmentData.WebStatus.equals("Loaded")){
            _stReceived.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stReceived.setTextColor(Color.parseColor("#ffffff"));
            _stLoaded.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stLoaded.setTextColor(Color.parseColor("#ffffff"));
        }
        if(_shipmentData.WebStatus.equals("Shipped")){
            _stReceived.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stReceived.setTextColor(Color.parseColor("#ffffff"));
            _stLoaded.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stLoaded.setTextColor(Color.parseColor("#ffffff"));
            _stShipped.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stShipped.setTextColor(Color.parseColor("#ffffff"));
        }
        if(_shipmentData.WebStatus.equals("Transit")){
            _stReceived.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stReceived.setTextColor(Color.parseColor("#ffffff"));
            _stLoaded.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stLoaded.setTextColor(Color.parseColor("#ffffff"));
            _stShipped.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stShipped.setTextColor(Color.parseColor("#ffffff"));
            _stTransit.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stTransit.setTextColor(Color.parseColor("#ffffff"));
        }
        if(_shipmentData.WebStatus.equals("Arrived")){
            _stReceived.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stReceived.setTextColor(Color.parseColor("#ffffff"));
            _stLoaded.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stLoaded.setTextColor(Color.parseColor("#ffffff"));
            _stShipped.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stShipped.setTextColor(Color.parseColor("#ffffff"));
            _stTransit.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stTransit.setTextColor(Color.parseColor("#ffffff"));
            _stArrived.setBackgroundColor(Color.parseColor(_shipmentData.StatusColor));
            _stArrived.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private void getAwbProducts(String url){

        Map<String, String> headers = new HashMap<>();

        headers.put("Content-type", "application/json");
        headers.put("UserToken", _userInfo.UserToken);
        AwbProductDataRequest request = new AwbProductDataRequest();

        request.ServiceAction = "awb_products";
        request.ParentID = Integer.parseInt(_shipmentData.AirWayBillID);
        request.ID = 0;
        request.PageIndex = 0;
        request.Offset = 0;
        request.PageSize = 0;
        request.Condition = request.new Condition();
        request.Condition.CustomerID = _userInfo.CustomerID;
        request.Condition.DataHub = true;
        request.Condition.CustomerWebUserID = _userInfo.UserID;
        Gson gson = new Gson();
        String body = gson.toJson(request);
        String response = RequestManager.post(url, headers, body);

        if (response.length() != 0) {
            Gson result = new Gson();
            _awbProducts= result.fromJson(response,AwbProductData[].class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fillAwbProducts(_awbProducts);
                }
            });
        }
    }

    private void fillAwbProducts(AwbProductData[] awbProducts){

        for(int i=0;i<awbProducts.length;i++) {
            View v = this.getLayoutInflater().inflate(R.layout.shipment_detail_awb_product_block, _llAwbProductContainer);
            LinearLayout llShipmentDetAWBProductBlock = (LinearLayout)v.findViewWithTag("llShipmentDetAWBProductBlock");
            llShipmentDetAWBProductBlock.setTag("llShipmentDetAWBBlock"+i);
            if((i%2)==0){llShipmentDetAWBProductBlock.setBackgroundColor(Color.parseColor("#ffffff"));}else{llShipmentDetAWBProductBlock.setBackgroundColor(Color.parseColor("#f7f7f7"));}

            TextView producer = (TextView)v.findViewWithTag("producer");
            producer.setTag("producer"+i);
            producer.setText(awbProducts[i].ProducerName);

            TextView product = (TextView)v.findViewWithTag("product");
            product.setTag("product"+i);
            product.setText(awbProducts[i].ProductName);

            TextView variety = (TextView)v.findViewWithTag("variety");
            variety.setTag("variety"+i);
            variety.setText(awbProducts[i].Variety);

            TextView label = (TextView)v.findViewWithTag("label");
            label.setTag("label"+i);
            label.setText(awbProducts[i].Label);

            TextView size = (TextView)v.findViewWithTag("size");
            size.setTag("size"+i);
            size.setText(awbProducts[i].Size);

            TextView expQty = (TextView)v.findViewWithTag("expQty");
            expQty.setTag("expQty"+i);
            expQty.setText(awbProducts[i].ExpQuantity);

            TextView assignedQty = (TextView)v.findViewWithTag("assignedQty");
            assignedQty.setTag("assignedQty"+i);
            assignedQty.setText(awbProducts[i].Quantity);
        }
    }

    private void fillAWBCharges(FreightShipmentData.AirOrderCharges[] awbCharges){

        if(awbCharges!=null) {
            for (int i = 0; i < awbCharges.length; i++) {
                if (Double.parseDouble(awbCharges[i].UnitSellPrice) > 0 && Double.parseDouble(awbCharges[i].Quantity) > 0) {
                    View v = this.getLayoutInflater().inflate(R.layout.shipment_detail_awb_charges_block, _llAwbChargeContainerLayout);
                    LinearLayout llShipmentDetAWBChargeBlock = (LinearLayout) v.findViewWithTag("llShipmentDetAWBChargeBlock");
                    llShipmentDetAWBChargeBlock.setTag("llShipmentDetAWBBlock" + i);
                    if ((i % 2) == 0) {
                        llShipmentDetAWBChargeBlock.setBackgroundColor(Color.parseColor("#ffffff"));
                    } else {
                        llShipmentDetAWBChargeBlock.setBackgroundColor(Color.parseColor("#f7f7f7"));
                    }

                    TextView charge = (TextView) v.findViewWithTag("charge");
                    charge.setTag("charge" + i);
                    charge.setText(awbCharges[i].ChargeName);

                    TextView unitPrice = (TextView) v.findViewWithTag("unitPrice");
                    unitPrice.setTag("unitPrice" + i);
                    unitPrice.setText("$" + Double.parseDouble(awbCharges[i].UnitSellPrice));

                    TextView quantity = (TextView) v.findViewWithTag("quantity");
                    quantity.setTag("quantity" + i);
                    quantity.setText(awbCharges[i].Quantity);

                    TextView extPrice = (TextView) v.findViewWithTag("extPrice");
                    extPrice.setTag("extPrice" + i);
                    extPrice.setText("$" + Double.parseDouble(awbCharges[i].Quantity) * Double.parseDouble(awbCharges[i].UnitSellPrice));
                }
            }
        }
    }

    private int manipulateStatusBarColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }
}
