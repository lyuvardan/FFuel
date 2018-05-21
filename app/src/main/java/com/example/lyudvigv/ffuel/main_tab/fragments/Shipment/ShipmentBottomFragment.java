package com.example.lyudvigv.ffuel.main_tab.fragments.Shipment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lyudvigv.ffuel.DataModel.AirportInfo;
import com.example.lyudvigv.ffuel.DataModel.FlightAwareData;
import com.example.lyudvigv.ffuel.DataModel.FlightAwareRequest;
import com.example.lyudvigv.ffuel.DataModel.FlightInfoWithAirportData;
import com.example.lyudvigv.ffuel.DataModel.FreightShipmentData;
import com.example.lyudvigv.ffuel.DataModel.ShipmentData;
import com.example.lyudvigv.ffuel.DataModel.ShipmentDataRequest;
import com.example.lyudvigv.ffuel.MyApplication;
import com.example.lyudvigv.ffuel.R;
import com.example.lyudvigv.ffuel.RequestManager;
import com.example.lyudvigv.ffuel.adapters.BottomShipmentAdapter;
import com.example.lyudvigv.ffuel.loginAndRegistration.UserInfo;
import com.example.lyudvigv.ffuel.main_tab.fragments.Map.MapFragment;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LyudvigV on 9/8/2017.
 */

public class ShipmentBottomFragment extends Fragment{
    private ListView _bottomShipmentList;
    private String _baseUrl;
    private String _dataHubUrl;
    private String _getShipmentsUrl;
    private String _getFlightAwareDataUrl;
    private String _getFlightInfoWithAirportDataUrl;
    private String _freightUrl;
    private BottomShipmentAdapter _adapter;
    private UserInfo _userInfo;
    private ShipmentData[] _shipments;
    private FreightShipmentData[] _freightShipments;
    private List<ShipmentData> _shipmentsFiltered;
    private FlightAwareData[] _flightAwareShipments;
    private List<FlightAwareData> _flightAwareShipmentsFiltered;
    private FlightInfoWithAirportData[] _flightInfoWithAirportDataList;
    private List<FlightInfoWithAirportData> _flightInfoWithAirportsFiltered;
    private MapFragment _mapFragment;
    private List<String> _wayPoints;
    private List<Double[]> _markers;

    private String _awbs;

    private List<FlightAwareRequest> _flightAwareRequestData;



    public static ShipmentBottomFragment newInstance() {
        ShipmentBottomFragment fragment = new ShipmentBottomFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shipment_bottom,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _bottomShipmentList = (ListView)getActivity().findViewById(R.id.lvBottomShipments);
        _adapter = new BottomShipmentAdapter(getContext(),R.layout.shipment_bottom_list_item);
        _baseUrl = getResources().getString(R.string.service_url);
        _dataHubUrl = getResources().getString(R.string.dataHub_url);
        _getShipmentsUrl = _baseUrl + "api/json/data/list";
        _getFlightAwareDataUrl = _dataHubUrl+"Mobile/GetFlightDetails";
        _getFlightInfoWithAirportDataUrl = _dataHubUrl + "Mobile/GetFlightAirportTimeZoneInfo";
        _freightUrl = getResources().getString(R.string.freight_url);
        _userInfo = ((MyApplication)getActivity().getApplication()).getUserInfo();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    getAwbShipments(_getShipmentsUrl);
                } catch (Exception e) {}
            }
        });
        thread.start();
    }

    private void getAwbShipments(String url) {
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-type","application/json");
        headers.put("UserToken",_userInfo.UserToken);

        ShipmentDataRequest request = new ShipmentDataRequest();

        request.ServiceAction = "Flight";
        request.Condition = request.new Condition();

        request.Condition.CreateDate = _userInfo.DataHubStartDate;
        request.Condition.DataHub = true;
        request.Condition.FlightCacheData = true;
        try {
            Gson g = new Gson();
            String response = RequestManager.post(url,headers,g.toJson(request));
            if(response.length()!=0)
            {
                _flightAwareRequestData = new ArrayList<>();
                _shipments = new Gson().fromJson(response, ShipmentData[].class);

                for(int i =0;i<_shipments.length;i++){
                    _awbs=_awbs+_shipments[i].AWBCode+"-"+_shipments[i].AirWayBillNo+",";

                    FlightAwareRequest far = new FlightAwareRequest();
                    far.AirWayBillID = _shipments[i].AirWayBillID;
                    far.AirWayBillNo = _shipments[i].AirWayBillNo;
                    far.Ident = _shipments[i].FlightCode;
                    far.Ident_2 = _shipments[i].FlightCode_2;
                    far.Ident_3 = _shipments[i].FlightCode_3;
                    far.FlightTime = _shipments[i].FlightTime;
                    far.FlightTime_2 = _shipments[i].FlightTime_2;
                    far.FlightTime_3 = _shipments[i].FlightTime_3;
                    _flightAwareRequestData.add(far);
                }

                _awbs = _awbs.substring(0, _awbs.length() - 1);

                Thread freightTread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        getFreightShipments(_freightUrl,_awbs);

                        Thread flightAwareThread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                getFlightAwareData(_getFlightAwareDataUrl, _flightAwareRequestData);

                                Thread timeZoneThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getFlightAirportTimeZoneInfo(_getFlightInfoWithAirportDataUrl);
                                    }
                                });

                                timeZoneThread.start();

                            }
                        });
                        flightAwareThread.start();
                    }
                });
                freightTread.start();



            }else {
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void getFreightShipments(String url, String awbs){

        Map<String,String> headers = new HashMap<>();
        headers.put("Content-type","application/json");
        JSONObject bodyParams = new JSONObject();
        try {
            bodyParams.accumulate("orgCode", "ABLE");
            bodyParams.accumulate("includeStatus", "true");
            bodyParams.accumulate("includeEDIData", "false");
            bodyParams.accumulate("includeCharges", "true");
            bodyParams.accumulate("requestType", "view");

            bodyParams.accumulate("awbs", awbs);

            String response = RequestManager.post(url,headers,bodyParams);

            if(response.length()!=0)
            {
                _freightShipments = new Gson().fromJson(response, FreightShipmentData[].class);

                mergeFreightWithWMS();

                _adapter.addAll(_shipments);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    _bottomShipmentList.setAdapter(_adapter);
                    }
                });
            }else {
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void getFlightAwareData(String url, List<FlightAwareRequest> list){

        Map<String,String> headers = new HashMap<>();

        headers.put("Content-type","application/json");
        headers.put("UserToken",_userInfo.UserToken);

        Gson gson = new Gson();
        String body = gson.toJson(list);
        try {
            String response = RequestManager.post(url,headers,body);
            if(response.length()!=0){
                _flightAwareShipments = new Gson().fromJson(response,FlightAwareData[].class);
            }
        }catch (Exception e){
            String a = e.getMessage();
        }
    }

    private void getFlightAirportTimeZoneInfo(String url){
        if(_flightAwareShipments!=null) {
            _flightInfoWithAirportDataList = new FlightInfoWithAirportData[_flightAwareShipments.length];
            for (int i = 0; i < _flightAwareShipments.length; i++) {
                FlightInfoWithAirportData data = new FlightInfoWithAirportData();
                data.ident = _flightAwareShipments[i].Ident;
                data.origin = new AirportInfo();
                data.origin.code = _flightAwareShipments[i].Origin;
                data.origin.icaoCode = _flightAwareShipments[i].OriginIcaoCode;
                data.destination = new AirportInfo();
                data.destination.code = _flightAwareShipments[i].Destination;
                data.destination.icaoCode = _flightAwareShipments[i].DestinationIcaoCode;
                _flightInfoWithAirportDataList[i] = data;
            }
            Map<String, String> headers = new HashMap<>();

            headers.put("Content-type", "application/json");
            headers.put("UserToken", _userInfo.UserToken);

            Gson gson = new Gson();
            String body = gson.toJson(_flightInfoWithAirportDataList);
            String response = RequestManager.post(url, headers, body);

            if (response.length() != 0) {
                _flightInfoWithAirportDataList = new Gson().fromJson(response, FlightInfoWithAirportData[].class);

                _wayPoints = new ArrayList<>();
                _markers = new ArrayList<Double[]>();
                _flightAwareShipmentsFiltered = new ArrayList<>();
                _flightInfoWithAirportsFiltered = new ArrayList<>();
                _shipmentsFiltered = new ArrayList<>();

                List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
                for (int i = 0; i < fragments.size(); i++) {
                    if (fragments.get(i) instanceof MapFragment) {
                        _mapFragment = (MapFragment) fragments.get(i);

                        mergeFlightAwareWithWMSFreight();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _mapFragment.fillMap(_flightAwareShipmentsFiltered ,_flightInfoWithAirportsFiltered, _shipmentsFiltered);
                                _adapter.notifyDataSetChanged();
                            }
                        });
                        break;
                    }
                }
            }
        }
    }

    private void mergeFreightWithWMS(){

        for(int i =0; i<_shipments.length;i++){

            for (int j=0;j<_freightShipments.length;j++){
                if(_shipments[i].AWBCode.equals(_freightShipments[j].AWBCode)&&_shipments[i].AirWayBillNo.equals(_freightShipments[j].AWBNumber))
                {
                    if(_freightShipments[j].AirOrderCharges!=null&&_shipments[i].IsBillToParty.equals("1")&&_userInfo.IsConsignee==false&&
                        (_freightShipments[j].OrderStatus.equals("Complete") || _freightShipments[i].OrderStatus.equals("Invoiced")))
                    {
                        _shipments[i].AirOrderChargesWms = _freightShipments[j].AirOrderCharges;
                    }
                    if(_freightShipments[j].CarrierStatus!=null) {

                        if (_freightShipments[j].CarrierStatus.toLowerCase().equals("landed")) {
                            _shipments[i].WebStatus = "Arrived";
                        }
                        else if(_freightShipments[j].CarrierStatus.toLowerCase().equals("active")) {
                            _shipments[i].WebStatus = "Transit";
                        }
                        else {
                            _shipments[i].WebStatus = _freightShipments[j].CarrierStatus;
                        }
                    }

                    if(_freightShipments[j].Flight1DepartureActual!=null){
                        _shipments[i].FlightTime = _freightShipments[j].Flight1DepartureActual;
                    }else if(_freightShipments[j].Flight1DepartureScheduled!=null){
                        _shipments[i].FlightTime = _freightShipments[j].Flight1DepartureScheduled;
                    }else if(_freightShipments[j].Flight1DateTime!=null){
                        _shipments[i].FlightTime = _freightShipments[j].Flight1DateTime;
                    }else {
                        _shipments[i].FlightTime = _shipments[i].FlightTime;
                    }


                    if(_freightShipments[j].Flight2DepartureActual!=null){
                        _shipments[i].FlightTime_2 = _freightShipments[j].Flight2DepartureActual;
                    }else if(_freightShipments[j].Flight1DepartureScheduled!=null){
                        _shipments[i].FlightTime_2 = _freightShipments[j].Flight2DepartureScheduled;
                    }else if(_freightShipments[j].Flight2DateTime!=null){
                        _shipments[i].FlightTime_2 = _freightShipments[j].Flight2DateTime;
                    }else {
                        _shipments[i].FlightTime_2 = _shipments[i].FlightTime_2;
                    }


                    if(_freightShipments[j].Flight3DepartureActual!=null){
                        _shipments[i].FlightTime_3 = _freightShipments[j].Flight3DepartureActual;
                    }else if(_freightShipments[j].Flight1DepartureScheduled!=null){
                        _shipments[i].FlightTime_3 = _freightShipments[j].Flight3DepartureScheduled;
                    }else if(_freightShipments[j].Flight3DateTime!=null){
                        _shipments[i].FlightTime_3 = _freightShipments[j].Flight3DateTime;
                    }else {
                        _shipments[i].FlightTime_3 = _shipments[i].FlightTime_3;
                    }

                    if(_freightShipments[j].ArrivalDateTime!=null)
                        _shipments[i].ArrivalTime = _freightShipments[j].ArrivalDateTime;

                    if(_freightShipments[j].Airport1Code!=null)
                        _shipments[i].DepartureAirportCode = _freightShipments[j].Airport1Code;
                    if(_freightShipments[j].Airport2Code!=null)
                        _shipments[i].AirportCode_2 = _freightShipments[j].Airport2Code;
                    if(_freightShipments[j].Airport3Code!=null)
                        _shipments[i].AirportCode_3 = _freightShipments[j].Airport3Code;
                    if(_freightShipments[j].ArrivalAirportCode!=null)
                        _shipments[i].DestinationAirportCode = _freightShipments[j].ArrivalAirportCode;


                    if(_freightShipments[j].AirCarrier1Code!=null)
                        _shipments[i].AirlineCode =_freightShipments[j].AirCarrier1Code;
                    if(_freightShipments[j].AirCarrier2Code!=null)
                        _shipments[i].AirlineCode_2 = _freightShipments[j].AirCarrier2Code;
                    if(_freightShipments[j].AirCarrier3Code!=null)
                        _shipments[i].AirlineCode_3 = _freightShipments[j].AirCarrier3Code;


                    if(_freightShipments[j].AirCarrier1Name!=null)
                        _shipments[i].AirlineName =_freightShipments[j].AirCarrier1Name;
                    if(_freightShipments[j].AirCarrier2Name!=null)
                        _shipments[i].AirlineName_2 =_freightShipments[j].AirCarrier2Name;
                    if(_freightShipments[j].AirCarrier3Name!=null)
                        _shipments[i].AirlineName_3 =_freightShipments[j].AirCarrier3Name;

                    if(_shipments[i].AirlineName_3!=null){
                        _shipments[i].FlightCount = 3;
                    }else if(_shipments[i].AirlineName_2!=null){
                        _shipments[i].FlightCount = 2;
                    }else {
                        _shipments[i].FlightCount = 1;
                    }

                    if(_freightShipments[j].Flight1Number!=null)
                        _shipments[i].FlightNo =_freightShipments[j].Flight1Number;
                    if(_freightShipments[j].Flight2Number!=null)
                        _shipments[i].FlightNo_2 =_freightShipments[j].Flight2Number;
                    if(_freightShipments[j].Flight3Number!=null)
                        _shipments[i].FlightNo_3 =_freightShipments[j].Flight3Number;

                    if(_freightShipments[j].Flight1DepartureScheduled!=null)
                        _shipments[i].Flight1DepartureScheduled=_freightShipments[j].Flight1DepartureScheduled;
                    if(_freightShipments[j].Flight2DepartureScheduled!=null)
                        _shipments[i].Flight2DepartureScheduled=_freightShipments[j].Flight2DepartureScheduled;
                    if(_freightShipments[j].Flight3DepartureScheduled!=null)
                        _shipments[i].Flight3DepartureScheduled=_freightShipments[j].Flight3DepartureScheduled;

                    if(_freightShipments[j].Flight1DepartureActual!=null)
                        _shipments[i].Flight1DepartureActual=_freightShipments[j].Flight1DepartureActual;
                    if(_freightShipments[j].Flight2DepartureActual!=null)
                        _shipments[i].Flight2DepartureActual=_freightShipments[j].Flight2DepartureActual;
                    if(_freightShipments[j].Flight3DepartureActual!=null)
                        _shipments[i].Flight3DepartureActual=_freightShipments[j].Flight3DepartureActual;

                    if(_freightShipments[j].Flight1ArrivalScheduled!=null)
                        _shipments[i].Flight1ArrivalScheduled=_freightShipments[j].Flight1ArrivalScheduled;
                    if(_freightShipments[j].Flight2ArrivalScheduled!=null)
                        _shipments[i].Flight2ArrivalScheduled=_freightShipments[j].Flight2ArrivalScheduled;
                    if(_freightShipments[j].Flight3ArrivalScheduled!=null)
                        _shipments[i].Flight3ArrivalScheduled=_freightShipments[j].Flight3ArrivalScheduled;

                    if(_freightShipments[j].Flight1ArrivalActual!=null)
                        _shipments[i].Flight1ArrivalActual=_freightShipments[j].Flight1ArrivalActual;
                    if(_freightShipments[j].Flight2ArrivalActual!=null)
                        _shipments[i].Flight2ArrivalActual=_freightShipments[j].Flight2ArrivalActual;
                    if(_freightShipments[j].Flight3ArrivalActual!=null)
                        _shipments[i].Flight3ArrivalActual=_freightShipments[j].Flight3ArrivalActual;


                }
            }
        }
    }

    private void mergeFlightAwareWithWMSFreight(){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat tzFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Date wmsFlightTime = null;
        Date wmsFlightTime2 = null;
        Date wmsFlightTime3 = null;

        for(int i=0; i < _shipments.length;i++){
            try{
                wmsFlightTime  = _shipments[i].FlightTime != null ? formatter.parse(_shipments[i].FlightTime):null;
                wmsFlightTime2 = _shipments[i].FlightTime_2 != null ? formatter.parse(_shipments[i].FlightTime_2):null;
                wmsFlightTime3 = _shipments[i].FlightTime_3 != null ? formatter.parse(_shipments[i].FlightTime_3):null;
            }
            catch (Exception e){
                e.getMessage();
            }

            for (int j=0; j< _flightAwareShipments.length;j++){

                try {
                    Date faDepTime = tzFormatter.parse(_flightAwareShipments[j].DepeartueTime);
                    String difHoursStr = _flightInfoWithAirportDataList[j].origin.timezoneOffset.split("\\:")[0];
                    Integer difHours = Integer.parseInt(difHoursStr);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(faDepTime);
                    cal.add(Calendar.HOUR, difHours);
                    Date faActDepDate = cal.getTime();

                    Integer diffTimes1 =null;
                    Integer diffTimes2 =null;
                    Integer diffTimes3 =null;

                    if(wmsFlightTime!=null ){
                        diffTimes1 = (int) ((wmsFlightTime.getTime() - faActDepDate.getTime()) / (1000 * 60 * 60));
                    }
                    if(wmsFlightTime2!=null){
                        diffTimes2 = (int) ((wmsFlightTime2.getTime() - faActDepDate.getTime()) / (1000 * 60 * 60));
                    }
                    if(wmsFlightTime3!=null){
                        diffTimes3 = (int) ((wmsFlightTime3.getTime() - faActDepDate.getTime()) / (1000 * 60 * 60));
                    }

                    if(_shipments[i].FlightCode!=null&&_shipments[i].FlightCode.equals(_flightAwareShipments[j].Ident)) {
                        if (Math.abs(diffTimes1) < 6) {
                            _shipments[i].DepartureAirportCode = _flightAwareShipments[j].Origin;
                            _shipments[i].DestinationAirportCode = _flightAwareShipments[j].Destination;
                            _shipments[i].WebStatus = _flightAwareShipments[j].WebStatus;
                            _flightAwareShipmentsFiltered.add(_flightAwareShipments[j]);
                            _flightInfoWithAirportsFiltered.add(_flightInfoWithAirportDataList[j]);
                            _shipmentsFiltered.add(_shipments[i]);
                            break;
                        }
                    }
                    if(_shipments[i].FlightCode_2!=null&&_shipments[i].FlightCode_2.equals(_flightAwareShipments[j].Ident)) {
                        if (Math.abs(diffTimes2) < 6) {
                            _shipments[i].DepartureAirportCode = _flightAwareShipments[j].Origin;
                            _shipments[i].DestinationAirportCode = _flightAwareShipments[j].Destination;
                            _shipments[i].WebStatus = _flightAwareShipments[j].WebStatus;
                            _flightAwareShipmentsFiltered.add(_flightAwareShipments[j]);
                            _flightInfoWithAirportsFiltered.add(_flightInfoWithAirportDataList[j]);
                            _shipmentsFiltered.add(_shipments[i]);
                            break;
                        }
                    }
                    if(_shipments[i].FlightCode_3!=null&&_shipments[i].FlightCode_3.equals(_flightAwareShipments[j].Ident)) {
                        if (Math.abs(diffTimes3) < 6) {
                            _shipments[i].DepartureAirportCode = _flightAwareShipments[j].Origin;
                            _shipments[i].DestinationAirportCode = _flightAwareShipments[j].Destination;
                            _shipments[i].WebStatus = _flightAwareShipments[j].WebStatus;
                            _flightAwareShipmentsFiltered.add(_flightAwareShipments[j]);
                            _flightInfoWithAirportsFiltered.add(_flightInfoWithAirportDataList[j]);
                            _shipmentsFiltered.add(_shipments[i]);
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }
}
