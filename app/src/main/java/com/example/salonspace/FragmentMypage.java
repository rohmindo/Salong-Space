package com.example.salonspace;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.Context.LOCATION_SERVICE;

public class FragmentMypage extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {
    private GoogleMap mgoogleMap;
    LocationManager locationManager;
    MapView mapView;
    ImageView img;
    double lng;
    double lat;
    String [] permission_list={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            requestPermissions(permission_list,0);
        }




        View v= (View)inflater.inflate(R.layout.fragment_mypage, container, false);

        img=v.findViewById(R.id.getloc);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("testt1",""+Address_current.latitude);
                getMyLocation();
                LatLng Address=new LatLng(lat,lng);
                mgoogleMap.moveCamera(CameraUpdateFactory.newLatLng(Address));
                MarkerOptions markerOptions=new MarkerOptions(); //?????? ??????
                Log.e("testlat",lat+"//"+lng);
                markerOptions.position(Address);
                markerOptions.title("?????????");  //???????????? ??????
                mgoogleMap.addMarker(markerOptions); //????????????
                //mgoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        });
        mapView = (MapView)v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);


        return v;
    }
    //????????? ????????????
    public void getMyLocation(){
        locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                return;
        }
        //????????? ???????????? ?????? ????????????.
        Location location1=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location location2=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // 1. ????????????, 2. GPS
        if(location1 != null) { setMyLocation(location1); }
        else if(location2!=null) { setMyLocation(location2); }

        // 1. ????????????, 2. GPS
        GetMyLocationListener listener=new GetMyLocationListener();
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)==true) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,10f,listener);
        } else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)==true) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10f,listener);
        }

    }

    public void setMyLocation(Location location){
        Log.d("test33","?????? :"+location.getLatitude());
        Log.d("test33","?????? :"+location.getLongitude());
        lat=location.getLatitude();
        lng=location.getLongitude();
    }
    //?????? ?????? ????????? ???????????? ???????????? ?????????
    class GetMyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull Location location) {
            setMyLocation(location);
            locationManager.removeUpdates(this);
        }
        @Override
        public void onProviderDisabled(String provider){

        }
        @Override
        public void onProviderEnabled(String provider){

        }
        @Override
        public void onStatusChanged(String provider,int status,Bundle extras){

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] resultcode){
        super.onRequestPermissionsResult(requestCode,permissions,resultcode);
    }


    @Override
    public void onMapReady(GoogleMap googlemap) {
        mgoogleMap = googlemap;
        final LatLng Address=new LatLng(37.283102,127.044884);
        MarkerOptions markerOptions=new MarkerOptions(); //?????? ??????
        markerOptions.position(Address);
        markerOptions.title("?????????");  //???????????? ??????
        googlemap.addMarker(markerOptions); //????????????
        googlemap.setOnMarkerClickListener(this);

        googlemap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mgoogleMap.moveCamera(CameraUpdateFactory.newLatLng(Address));
                mgoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }); // ?????? ??? ?????? ???????????? ????????? ?????? ????????????
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(getContext(), PopupActivity.class);
        intent.putExtra("data", "???????????? ??????????????? ????????????.");
        startActivityForResult(intent, 1);
        return true;
    }
    public void BTN2(View v){
        final LatLng Address=new LatLng(40.283102,147.044884);
        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLng(Address));
        mgoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}
