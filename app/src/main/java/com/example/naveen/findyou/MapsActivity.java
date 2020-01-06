package com.example.naveen.findyou;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker mark=null;
    LatLng prev,cur;
    public String addr,st;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Firebase.setAndroidContext(this);
        String user = getIntent().getExtras().getString("name");
        final String user1 = getIntent().getExtras().getString("name1");
        Firebase fm = new Firebase("https://androidtrainee-7e562.firebaseio.com/user/usernames/" + user + "/location");
        fm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String loc = dataSnapshot.getValue(String.class);
                int ind = loc.indexOf(",");
                double lat = Double.parseDouble(loc.substring(0, ind));
                double lon = Double.parseDouble(loc.substring(ind + 1));
                LatLng ll = new LatLng(lat, lon);
                Geocoder gc=new Geocoder(getApplicationContext());
                try {
                    List<android.location.Address> address=gc.getFromLocation(lat,lon,1);
                    st=address.get(0).getLocality()+",";
                    st=st+address.get(0).getFeatureName()+","+address.get(0).getAddressLine(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(mark!=null)
                {
                    cur=ll;
                    mark.remove();
                    mMap.addMarker(new MarkerOptions().position(prev).title(user1 + " recorded location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).snippet(addr));
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(prev,cur)
                            .width(10)
                            .color(Color.BLUE));
                }
                addr=st;
                prev=ll;
                mark=mMap.addMarker(new MarkerOptions().position(ll).title(user1 + " current location"+st).icon(BitmapDescriptorFactory.fromResource(R.mipmap.man)).snippet(st));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,14.2f));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
