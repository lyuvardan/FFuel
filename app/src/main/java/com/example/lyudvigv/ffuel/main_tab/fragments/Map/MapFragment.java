package com.example.lyudvigv.ffuel.main_tab.fragments.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lyudvigv.ffuel.DataModel.FlightAwareData;
import com.example.lyudvigv.ffuel.DataModel.FlightInfoWithAirportData;
import com.example.lyudvigv.ffuel.DataModel.ShipmentData;
import com.example.lyudvigv.ffuel.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by LyudvigV on 9/8/2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap _googleMap;
    private MapView _mapView;
    private Map<Marker,FlightAwareData> _markerMapData;
    private Map<Marker,List<ShipmentData>> _markerMapShipmentData;
    private Map<String,List<ShipmentData>> _mapShipmentData;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prepareMap(view);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        _googleMap = googleMap;

        _googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        //_googleMap.setMyLocationEnabled(true);
        //_googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.7558, 37.6173), 15));
        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);//MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);//MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else {
                // No explanation needed; request the permission


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else {
            _googleMap.setMyLocationEnabled(true);


            _googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                   // _googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));
                    _googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15));
                }
            });
            // Permission has already been granted
        }
        /*if(getContext().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {


            _googleMap.setMyLocationEnabled(true);

            _googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    _googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));
                }
            });
        }*/

        _markerMapData = new HashMap<>();
        _markerMapShipmentData = new HashMap<>();
        _mapShipmentData=new HashMap<>();
    }

    private void prepareMap(View view){
        Locale locale = new Locale("ru_RU");
        Locale.setDefault(locale);
        Configuration config = new Configuration();

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN){
            config.setLocale(locale);
            getContext().createConfigurationContext(config);
        }else { //deprecated
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }

        _mapView = (MapView) view.findViewById(R.id.mapView);
        if(_mapView!=null){
            _mapView.onCreate(null);
            _mapView.onResume();
            _mapView.getMapAsync(this);
        }
    }

    public void fillMap(List<FlightAwareData> flightAwareData, List<FlightInfoWithAirportData> airportInfo, List<ShipmentData> shipments){

        for(int i=0;i<flightAwareData.size();i++){
            Gson gson = new Gson();
            WayPoint[] wps = gson.fromJson(flightAwareData.get(i).Waypoint ,WayPoint[].class);

           /* wps[0].lat = airportInfo.get(i).origin.latitude;
            wps[0].lng = airportInfo.get(i).origin.longitude;

            wps[wps.length-1].lat = airportInfo.get(i).destination.latitude;
            wps[wps.length-1].lng = airportInfo.get(i).destination.longitude;*/

            Double markerLat = Double.parseDouble(flightAwareData.get(i).Lattitude);
            Double markerLng = Double.parseDouble(flightAwareData.get(i).Longitude);

            LatLng[] latLngs = new LatLng[wps.length];
            List<Double> distances = new ArrayList<>();

            for(int j=0;j<wps.length;j++){
                latLngs[j]=new LatLng(wps[j].lat,wps[j].lng);
                distances.add(Math.sqrt(Math.abs(markerLat-wps[j].lat)*Math.abs(markerLat-wps[j].lat)+Math.abs(markerLng-wps[j].lng)*Math.abs(markerLng-wps[j].lng)));
            }

            Collections.sort(distances);

            Double latitude=0.0;
            Double longitude=0.0;
            Double latitude1=0.0;
            Double longitude1=0.0;

            for (int j=0;j<wps.length;j++){
                if(Math.abs(Math.sqrt(Math.abs(markerLat-wps[j].lat)*Math.abs(markerLat-wps[j].lat)+Math.abs(markerLng-wps[j].lng)*Math.abs(markerLng-wps[j].lng))- distances.get(0))==0){
                    latitude = wps[j].lat;
                    longitude = wps[j].lng;
                    break;
                }
            }

            int index = 0;
            for(int j=0;j<wps.length;j++){
                if(latitude==wps[j].lat&&longitude==wps[j].lng){
                    index=j;
                    break;
                }
            }

            if(index<wps.length-1) {
                latitude1 = wps[index+1].lat;
                longitude1 = wps[index+1].lng;
            }else {
                latitude1 = wps[index].lat;
                longitude1 = wps[index].lng;
            }


            View planeTitleLayout = ((LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.plane_title_layout, null);

            ViewGroup vgPlaneTitleLayout = (ViewGroup) planeTitleLayout;
            TextView tvFlightInfo =  (TextView) vgPlaneTitleLayout.getChildAt(0);
            tvFlightInfo.setText(flightAwareData.get(i).Ident + " "+flightAwareData.get(i).AircraftType+"\n"+airportInfo.get(i).origin.code+" "+airportInfo.get(i).destination.code);

            Bitmap bitmap = createDrawableFromView(getActivity(), planeTitleLayout);

            MarkerOptions moPlaneTitile = new MarkerOptions().position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory
                .fromBitmap(bitmap));

            Marker mt = _googleMap.addMarker(moPlaneTitile);


            MarkerOptions moPlane =new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_plane))
                    .rotation(orientationAngle(latitude, longitude, latitude1, longitude1));

            Marker m = _googleMap.addMarker(moPlane);
            //m.setTag(i+"");

            _markerMapData.put(m,flightAwareData.get(i));
            _markerMapData.put(mt,flightAwareData.get(i));


            //for (ShipmentData shipmentData : shipments){
            String key = shipments.get(i).FlightCode;
            if(_mapShipmentData.containsKey(key)){

                List<ShipmentData> list = _mapShipmentData.get(key);
                list.add(shipments.get(i));

            }else{
                List<ShipmentData> list = new ArrayList<ShipmentData>();
                list.add(shipments.get(i));
                _mapShipmentData.put(key, list);
                _mapShipmentData.put(key,list);
            }
            //}

            _markerMapShipmentData.put(m,_mapShipmentData.get(key));
            _markerMapShipmentData.put(mt,_mapShipmentData.get(key));



            //shipments.stream().collect(Collectors.toCollection())

            //_markerMapShipmentData.put(m,shipments.get(i));
            //_markerMapShipmentData.put(mt,shipments.get(i));

            View airportTitleLayout = ((LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.airport_title_layout, null);

            ViewGroup vgairportTitleLayout = (ViewGroup) airportTitleLayout;

            TextView orAirportTitle =  (TextView) vgairportTitleLayout.getChildAt(0);
            orAirportTitle.setText(airportInfo.get(i).origin.code);
            Bitmap bitmapOrAir = createDrawableFromView(getActivity(), airportTitleLayout);
            MarkerOptions moOrAirTitile = new MarkerOptions().position(new LatLng(airportInfo.get(i).origin.latitude, airportInfo.get(i).origin.longitude))
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(bitmapOrAir));
            _googleMap.addMarker(moOrAirTitile);

            TextView destAirportTitle =  (TextView) vgairportTitleLayout.getChildAt(0);
            destAirportTitle.setText(airportInfo.get(i).destination.code);
            Bitmap bitmapDestAir = createDrawableFromView(getActivity(), airportTitleLayout);
            MarkerOptions moDestAirTitile = new MarkerOptions().position(new LatLng(airportInfo.get(i).destination.latitude, airportInfo.get(i).destination.longitude))
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(bitmapDestAir));
            _googleMap.addMarker(moDestAirTitile);


            MarkerOptions originPoint = new MarkerOptions()
                    .position(new LatLng(airportInfo.get(i).origin.latitude, airportInfo.get(i).origin.longitude))
                    .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_airport));

            MarkerOptions destinationPoint = new MarkerOptions()
                    .position(new LatLng(airportInfo.get(i).destination.latitude, airportInfo.get(i).destination.longitude))
                    .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_airport));

            _googleMap.addMarker(originPoint);
            _googleMap.addMarker(destinationPoint);



            _googleMap.addPolyline(new PolylineOptions()
                    .add(Arrays.copyOfRange(latLngs, 0, index+1))
                    .width(5)
                    .color(Color.RED));

            _googleMap.addPolyline(new PolylineOptions()
                    .add(Arrays.copyOfRange(latLngs, index, latLngs.length))
                    .width(1)
                    .color(Color.RED));
        }
        if(flightAwareData.size()>0) {
            _googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(flightAwareData.get(0).Lattitude), Double.parseDouble(flightAwareData.get(0).Longitude)), 3));
        }

        _googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                for (Marker m :_markerMapData.keySet()) {
                      if(marker.equals(m)){
                          GSDetailDialog dialog = new GSDetailDialog(getContext(),_markerMapData.get(m),_markerMapShipmentData.get(m));
                          dialog.show();
                      }
                }
                return true;
            }
        });
    }

    private static class WayPoint{
        Double lat;
        Double lng;
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (context != null) {
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(displayMetrics);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
            view.layout(0, 0, displayMetrics.widthPixels,
                    displayMetrics.heightPixels);
            view.buildDrawingCache();
            Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                    view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        }
        return null;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private float orientationAngle(double lat1, double lng1, double lat2, double lng2){
        Location prevLoc = new Location(LocationManager.GPS_PROVIDER);
        prevLoc.setLatitude(lat1);
        prevLoc.setLongitude(lng1);
        Location newLoc = new Location(LocationManager.GPS_PROVIDER);
        newLoc.setLatitude(lat2);
        newLoc.setLongitude(lng2);
        return prevLoc.bearingTo(newLoc);
    }
}
