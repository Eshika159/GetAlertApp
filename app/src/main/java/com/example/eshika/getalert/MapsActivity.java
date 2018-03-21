package com.example.eshika.getalert;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    PlaceAutocompleteFragment placeAutoComplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        View childView=(LinearLayout) placeAutoComplete.getView();
        ImageView searchIcon=(ImageView) childView.findViewById(R.id.place_autocomplete_search_button);
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.menu));
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build();
        placeAutoComplete.setFilter(typeFilter);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.d("Maps", "Place selected: " + place.getName());
                getPlaceonMap(place);
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void getPlaceonMap(Place place)
    {

        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(place.getLatLng()).title(place.getName().toString());
      //  LatLng latlng=place.getLatLng();
  mMap.addMarker(markerOptions);
  mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
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

        // Add a marker in Sydney and move the camera
      //  LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
