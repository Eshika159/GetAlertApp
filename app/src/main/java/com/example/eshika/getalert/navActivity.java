package com.example.eshika.getalert;

import android.Manifest;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class navActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
            //GoogleMap.OnMarkerClickListener{

            private GoogleMap mMap;
            private GoogleApiClient googleApiClient;
            private Location lastlocation;
            private LocationRequest locationRequest;
            Marker geoFenceMarker;
            Marker locationMarker;

    PlaceAutocompleteFragment placeAutoComplete;

    private static final int REQ_CODE=999;
    private static final int UPDATE_INTERVAL=5000; //5sec
    private static final int FASTEST_INTERVAL=3000; //3sec



    List<CustomList> list=new ArrayList<CustomList>();
    DbHelper db=new DbHelper(this);
   // Context context=this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("GetAlert");
        setSupportActionBar(toolbar);

      //  new Geofence_history(getApplicationContext());

            createGoogleApi();


        //Maps functionality


        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
      //  View childView = (LinearLayout) placeAutoComplete.getView();
        //ImageView searchIcon = (ImageView) childView.findViewById(R.id.place_autocomplete_search_button);
        //searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.menu));
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build();
        placeAutoComplete.setFilter(typeFilter);

        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected( Place place) {

                    Log.d("Maps", "Place selected: " + place.getName());
                 getPlaceonMap(place);


                }

                @Override
                public void onError(Status status) {
                    Log.d("Maps", "An error occurred: " + status);
                   // Toast.makeText(navActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                }
            });


            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


        /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
}

            private void createGoogleApi() {
                     if(googleApiClient==null){
                     googleApiClient=new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }

            }

            @Override
            protected void onStop() {
                super.onStop();
                googleApiClient.disconnect();
            }

            @Override
            protected void onStart() {
                super.onStart();
                googleApiClient.connect();

            }

    public void getPlaceonMap(final Place place)
             {

             AlertDialog.Builder builder= new AlertDialog.Builder(navActivity.this);
             final View mview= getLayoutInflater().inflate(R.layout.alert_dialog,null);
             final EditText name=(EditText)mview.findViewById(R.id.entergeoname);
              Button button=(Button)mview.findViewById(R.id.add_geofence);
              builder.setView(mview);
             final AlertDialog dialog=builder.create();
              dialog.show();
              button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().isEmpty())
                      {
                   //list needed when adapter is set thatbtoo from db
                   // list.add(new CustomList(place.getName().toString(),place.getLatLng().latitude,place.getLatLng().longitude));
                  /*  MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(place.getLatLng()).title(place.getName().toString());
                   //  LatLng latlng=place.getLatLng().;
                    //marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));
                    mMap.addCircle(new CircleOptions().center(place.getLatLng()).strokeColor(Color.RED).fillColor(0x220000FF).radius(500).strokeWidth(5.0f));
                    mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                */

//place.getName().toString()
                          db.addPlace(name.getText().toString(),place.getLatLng().latitude,place.getLatLng().longitude);
                          dialog.dismiss();
                          markerForgeofence(place.getLatLng());
                          startGeoFence();
                }
                else {
                    Toast.makeText(navActivity.this, "try again ", Toast.LENGTH_SHORT).show();
                    }

            }
        });

}
     private void markerLocation(LatLng latLng) {
        String title="My Location"+latLng.latitude+","+latLng.longitude;
        MarkerOptions markerOptions=new MarkerOptions().title(title)
                .position(latLng);
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentmarker));
        if(mMap!=null){
            if(locationMarker!=null){
                locationMarker.remove();
            }
            locationMarker=mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f));
        }

    }

    private void markerForgeofence(LatLng latLng) {
        String title="MarkerforGeofence :"+latLng.latitude+","+latLng.longitude;
        MarkerOptions markerOptions=new MarkerOptions().title(title)
                .position(latLng)
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.fence));
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        if(mMap!=null){
            if(geoFenceMarker!=null)
                geoFenceMarker.remove();

            geoFenceMarker=mMap.addMarker(markerOptions);

        }

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.history) {

            Intent intent=new Intent(getApplicationContext(),Geofence_history.class);
            startActivity(intent);


        }
        else if (id == R.id.nav_manage)
        {
           // Intent intent  = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
             //       "mailto:","eshikagupta159@gmail.com", null));
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"+"eshikagupta159@gmail.com"));
           // intent.putExtra(Intent.EXTRA_EMAIL,"");
            intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback for GETALERT");
          //  intent.setType("message/rfc822");
            Intent chooser=Intent.createChooser(intent,"Send Mail");
            startActivity(chooser);

        }

        else if (id == R.id.nav_share)
        {

            String lat=String.valueOf(lastlocation.getLatitude());
            String lng=String.valueOf(lastlocation.getLongitude());

            Intent intent=new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms","",null));
             intent.putExtra("sms_body","My Current Location:  http://maps.google.com?q="+lat+","+lng);
             startActivity(intent);


        } else if (id == R.id.nav_send)
        {
                 clearGeoFence();
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;

    }

   /* private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    } */

            @Override
            public void onConnected(@Nullable Bundle bundle) {
            Log.d("onconnected:","connected");
            getLastKnownLocation();
            recoverGeofenceMarker();

            }


            @Override
            public void onConnectionSuspended(int i) {

            }

            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.d("onconnected:","Failed");
            }



    @Override
    public void onLocationChanged(Location location) {
           lastlocation=location;
            writeActualLocation(location);

            }


    private void getLastKnownLocation()
       {

        if(checkPermission())
        {
            lastlocation=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(lastlocation!=null)
            {
                writeActualLocation(lastlocation);
                startLocationUpdates();
            }

            else{
                Log.d("TAG","no location yet");
                startLocationUpdates();
            }
        }

        else{
            askPermission();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode)
        {

            case REQ_CODE:{
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    getLastKnownLocation();
                }
                else
                    {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                  break;

            }
        }



    }

            private void startLocationUpdates() {

            locationRequest=new LocationRequest().create()
                    .setFastestInterval(FASTEST_INTERVAL)
                    .setInterval(UPDATE_INTERVAL)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if(checkPermission())
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        
            }

            private boolean checkPermission(){

            return(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED);

             }


            private void askPermission() {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQ_CODE);
            }

            private void writeActualLocation(Location lastlocation) {
                markerLocation(new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude()));
            }




    private  static final long GeoFence_Duration=60*60*1000;
    private static final String GEOFENCE_REQID="My Geofence";
    private static final float GEOFENCE_RADIUS=500.0f;



    private void startGeoFence() {
        if(geoFenceMarker!=null){
            Geofence geofence=createGeoFence(geoFenceMarker.getPosition(),GEOFENCE_RADIUS);
            GeofencingRequest geofencingRequest=createfenceRequest(geofence);
            addGeofence(geofencingRequest);
        }
        else {
            Log.i("INFO:","Marker is null");
        }
    }


    private Geofence createGeoFence(LatLng latLng,float radius){
                return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQID)
                .setExpirationDuration(GeoFence_Duration)
                .setCircularRegion(latLng.latitude,latLng.longitude,radius)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

    }

    //geofencerequest triggered when geofence is initialised and passed

    private GeofencingRequest createfenceRequest(Geofence geofence){
                 return new GeofencingRequest.Builder()
                 .addGeofence(geofence)
                 .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                 .build();

    }

    private PendingIntent geoFencependingIntent;
    private static final int GEQFENCE_RECODE=0;

    private PendingIntent createGeoFencePendingIntent()
    {
        if(geoFencependingIntent!=null)
            return geoFencependingIntent;
        Intent intent=new Intent(this,GeofenceTransitionService.class);
        return PendingIntent.getService(this,GEQFENCE_RECODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);

    }

    //add geofence request to device monitoring list

    private void addGeofence(GeofencingRequest request){
       if(checkPermission()){
           LocationServices.GeofencingApi.addGeofences(googleApiClient,request,
                   createGeoFencePendingIntent()).setResultCallback(new ResultCallback<Status>() {
               @Override
               public void onResult(@NonNull Status status) {
                    if(status.isSuccess()){
                        saveGeofence();
                           drawGeofence();}
                       else {
                        Toast.makeText(getApplicationContext(),"Failed "+status,Toast.LENGTH_SHORT).show();
                    }

                   }

           });
       }

    }



    private Circle geofenceLimits;
    private void drawGeofence() {
        if(geofenceLimits!=null)
            geofenceLimits.remove();

        CircleOptions circleOptions=new CircleOptions()
                .center(geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50,70,70,70))
                .fillColor(Color.argb(100,150,150,150))
                .radius(GEOFENCE_RADIUS);
        geofenceLimits=mMap.addCircle(circleOptions);

    }


  //  public static Intent makeNotificationIntent(Context applicationContext, String transitionDetails) {
  private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent( context, navActivity.class );
        intent.putExtra( NOTIFICATION_MSG, msg );
        return intent;
    }




    private final String GEOFENCE_LAT="Geofence Lat";
    private final String GEOFENCE_LNG="Geofence Lng";


    private void saveGeofence(){
        SharedPreferences sharedPref=getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putLong(GEOFENCE_LAT, Double.doubleToRawLongBits( geoFenceMarker.getPosition().latitude ));
        editor.putLong(GEOFENCE_LNG, Double.doubleToRawLongBits( geoFenceMarker.getPosition().longitude ));
              editor.apply();

    }
    private void recoverGeofenceMarker() {
       // Log.d(TAG, "recoverGeofenceMarker");
        SharedPreferences sharedPref = getPreferences( Context.MODE_PRIVATE );

        if ( sharedPref.contains( GEOFENCE_LAT ) && sharedPref.contains( GEOFENCE_LNG )) {
            double lat = Double.longBitsToDouble( sharedPref.getLong( GEOFENCE_LAT, -1 ));
            double lon = Double.longBitsToDouble( sharedPref.getLong( GEOFENCE_LNG, -1 ));
            LatLng latLng = new LatLng( lat, lon );
            markerForgeofence(latLng);
            drawGeofence();
        }}

    private void clearGeoFence(){
        LocationServices.GeofencingApi.removeGeofences(googleApiClient,createGeoFencePendingIntent()).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess())
                    removeGeofenceDraw();
                SharedPreferences sharedPref = getPreferences( Context.MODE_PRIVATE );
                SharedPreferences.Editor editor=sharedPref.edit();
editor.remove(GEOFENCE_LAT);
editor.remove(GEOFENCE_LNG);
editor.apply();



            }
        });
    }

    private void removeGeofenceDraw() {
        if(geoFenceMarker!=null)
            geoFenceMarker.remove();
        if(geofenceLimits!=null)
            geofenceLimits.remove();
    }

}



//   @Override
 /*   public boolean onMarkerClick(Marker marker) {

        return false;
    } */

