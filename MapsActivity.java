package com.jcdev.parking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Debug;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.util.DebugUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback
{
//GoogleMap.OnCameraChangeListener
    private GoogleMap mMap;
    private Marker marker;
    private Marker marker1;
    private Marker marker2;
    private Polyline pLine;
    private float zoom=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       /* SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        */
      initMap();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Home and move the camera
        LatLng home = new LatLng(39.085956, -77.150646);
        final LatLng post1 = new LatLng(39.085311, -77.150985);
        final LatLng post2 = new LatLng(39.085829, -77.150996);
        final Marker marker;
        Marker marker1;
        Marker marker2;
        //Location location = googleMap.get;
        //double lat= location.getLatitude();
        //double lng = location.getLongitude();
        //LatLng ll = new LatLng(lat, lng);
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 20));

        //mMap.addMarker(new MarkerOptions().position(home).title("Asi colocaremos las marcas de Free Parking"));
        //setMarket("Free Parking",39.085560, -77.151004);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener(){
                                           @Override
                                           public void onCameraChange(CameraPosition cameraPosition) {
                                               //float zoom = mMap.getMaxZoomLevel();
                                               float zoom=0;

                                               zoom = cameraPosition.zoom;

                                               if(zoom>=15) {
                                                   if(getZoom()==zoom) {
                                                       setZoom(zoom);
                                                   }
                                                   else{
                                                       removeMarker();
                                                       setLine("Free 2 hour parking", post1, post2);
                                                   }
                                               }else {
                                                   setMarker("Free Parking", 39.085560, -77.151004);
                                                   removeLine();
                                               }
                                               setZoom(zoom);
                                               //Toast.makeText(this,"", Toast.LENGTH_SHORT).show();

                                           }
        });

        // locationUser(mMap);

        // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home,14));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);//Makes the users current location visible by displaying a blue dot.



        LocationManager lm=(LocationManager)getSystemService(LOCATION_SERVICE);//use of location services by firstly defining location manager.
        String provider=lm.getBestProvider(new Criteria(), true);

        if(provider==null){
            //onProviderDisabled(provider);
            return;
        }
        Location location=lm.getLastKnownLocation(provider);


        LatLng latlng=new LatLng(location.getLatitude(),location.getLongitude());// This methods gets the users current longitude and latitude.

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));//Moves the camera to users current longitude and latitude
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,(float) 14.6));//Animates camera and zooms to preferred state on the user's current location.
        recoverMap();

    }



    private void setMarker(String message, double lat, double lng){
        if (marker != null) {
            marker.remove();
        }

        MarkerOptions options = new MarkerOptions()
                .title(message)
                .position(new LatLng(lat,lng))
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_twohourparking));
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN));
        marker = mMap.addMarker(options);

    }

    private void setLine(String message, LatLng X1, LatLng X2){
        if (marker1 != null) {
            marker1.remove();
            marker2.remove();
            pLine.remove();
        }
        MarkerOptions options = new MarkerOptions()
                .title(message)
                .anchor(.5f,.5f)
                //.alpha(0.8f)
                .rotation(2.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_test4));

                //.icon(BitmapDescriptorFactory.defaultMarker(
                //        BitmapDescriptorFactory.HUE_BLUE));
        marker1 = mMap.addMarker(options.position(X1));
        marker2 = mMap.addMarker(options.position(X2));
        PolylineOptions pOptions = new PolylineOptions()
                .add(marker1.getPosition())
                .add(marker2.getPosition())
                .color(Color.BLUE)
                .width(8);
        ;
        pLine = mMap.addPolyline(pOptions);
    }
    private void removeMarker(){
        if (marker != null) {
            marker.remove();
        }
    }
    private void removeLine(){
        if (pLine != null) {
            marker1.remove();
            marker2.remove();
            pLine.remove();
        }
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }


    public float getZoom() {
        return zoom;
    }

    @Override
    protected void onStop() {
        super.onStop();
        MapStateManager mgr = new MapStateManager(this);
        mgr.saveMapState(mMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        //mapFragment.onResume();
        //initMap();

        MapStateManager mgr = new MapStateManager(this);
       // mMap= mgr.getSavedMap();
        CameraPosition position = mgr.getSavedCameraPosition();
        if(position != null && mMap != null)
        {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            //Toast.makeText(this, "" + position.toString(),Toast.LENGTH_LONG);
            mMap.moveCamera(update);
        }


    }

    private boolean initMap(){
        if (mMap == null){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            //mapFragment.getMapAsync(this);
            mapFragment.getMapAsync(this);
        }
        return (mMap != null);
    }

    private void recoverMap(){
        MapStateManager mgr = new MapStateManager(this);
        // mMap= mgr.getSavedMap();
        CameraPosition position = mgr.getSavedCameraPosition();
        if(position != null && mMap != null)
        {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            //Toast.makeText(this, "" + position.toString(),Toast.LENGTH_LONG);
            mMap.moveCamera(update);
        }
    }
    /*
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        //float zoom = mMap.getMaxZoomLevel();
        setMarket("Free Parking",39.085560, -77.151004);
        float zoom = cameraPosition.zoom;
        Toast.makeText(this,"zoom", Toast.LENGTH_SHORT).show();
    }
*/
}
