package com.r3tr0.obdiiscantool;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TripActivity extends FragmentActivity implements OnMapReadyCallback,RoutingListener {

    private GoogleMap mMap;


    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    //widgets

    private ImageView mGps;
    private Button mStart,mEnd,mCancel ;

 private DatabaseReference ref2 ;
    detectEndtrip detectEndtrip1 = new detectEndtrip();


    //variables
    private int mDistanceToDestination,mDurationOfTrip,TripCounter=0 ;

    private Boolean clicked =false ;
    private String myLocationAddressName,myDestinationAddressName ;




    //private PlaceAutocompleteAdapter placeAutocompleteAdapter;

    Location mLastLocation,mDestination=new Location("");
    LocationRequest mLocationRequest;
    LatLng mDestinationLatLng=null  ;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);


        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);



        detectEndtrip1.start();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        polylines = new ArrayList<>();

        mGps=(ImageView)findViewById(R.id.ic_gps);


        mStart=(Button)findViewById(R.id.StartTrip);
        mEnd=(Button)findViewById(R.id.EndTrip);
        mCancel=(Button)findViewById(R.id.CancelTrip);




        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDestinationLatLng!=null)
                {


                    mLocationRequest.setInterval(100);
                    mLocationRequest.setFastestInterval(100);
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);




                    LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18f));

                    if(x ==  0 && mLastLocation!=null)
                    {
                        geoLocate(mLastLocation);
                        Toast.makeText(TripActivity.this, myLocationAddressName, Toast.LENGTH_SHORT).show();

                        x=1 ;

                    }

                    getRouteToMarker(mDestinationLatLng);
                    //mStart.setVisibility(View.GONE);
                    mEnd.setVisibility(View.VISIBLE);
                    mCancel.setVisibility(View.GONE);

                    //detectEndtrip detectEndtrip1 = new detectEndtrip();


                }

            }
        });


        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erasePolyLines();
                mDestinationLatLng=null ;
                mStart.setVisibility(View.VISIBLE);
                mEnd.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);

            }
        });

        mEnd.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {

                clicked=true;
                endTrip();
                erasePolyLines();
            }
        });

    }





    private void init(){

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                // Log.i(TAG, "Place: " + place.getName());

                mDestinationLatLng= place.getLatLng();
                myDestinationAddressName=place.getAddress().toString();

                mDestination.setLatitude(mDestinationLatLng.latitude);
                mDestination.setLongitude(mDestinationLatLng.longitude);

               // getRouteToMarker(place.getLatLng());

                Toast.makeText(getApplicationContext(),place.getName(),Toast.LENGTH_LONG).show();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),18f));

                MarkerOptions options=new MarkerOptions().position(place.getLatLng()).title("destination");
                mMap.addMarker(options);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                //  Log.i(TAG, "An error occurred: " + status);
            }
        });




/*
        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        ||actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == event.ACTION_DOWN
                        ||event.getAction() == event.KEYCODE_ENTER)
                {

                    geoLocate(address) ;
                    Toast.makeText(TripActivity.this, "Address Found", Toast.LENGTH_SHORT).show();

                }

                return false ;
            }
        });
*/
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

    }



    private void getRouteToMarker(LatLng destination)
    {

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()), destination)
                .build();
        routing.execute();

    }


    int x = 0 ;
    private void geoLocate(Location location)
    {

        Geocoder geocoder=new Geocoder(TripActivity.this, Locale.getDefault());


        try {
            List<Address> myList = geocoder.getFromLocation(Double.parseDouble(String.valueOf(location.getLatitude())),Double.parseDouble(String.valueOf(location.getLongitude())), 1);

            Address address = (Address) myList.get(0);
            myLocationAddressName=address.getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

   /*
    private void geoLocate(Address input_address){

        /*
        String search = mSearch.getText().toString();

        Geocoder geocoder=new Geocoder(TripActivity.this);

        List<Address> list = new ArrayList<>();

        try {

            list = geocoder.getFromLocationName(search,1);


        }catch (IOException e )
        {
            Toast.makeText(TripActivity.this, "geoLocation Exception"+e.getMessage(), Toast.LENGTH_SHORT).show();


        }

  //      if(list.size()>0)
    //    {
            //Address location=address;




            Toast.makeText(TripActivity.this, "Address Found"+ this.address.toString(), Toast.LENGTH_SHORT).show();
    //    }

    }
*/


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getCurrentLocation();


    }

    private void getCurrentLocation(){

        mLocationRequest = new LocationRequest();
       // mLocationRequest.setInterval(100);
        //mLocationRequest.setFastestInterval(100);
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
                init();


            } else {
                checkLocationPermission();
            }


        }else {

            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);

            init();

        }



    }
    private void checkLocationPermission() {

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(TripActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            }
            else{
                ActivityCompat.requestPermissions(TripActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    LocationCallback mLocationCallback=new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            for (Location location : locationResult.getLocations()) {

                if (getApplicationContext() != null) {


                    mLastLocation = location;
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18f));


                }



            }
        }


    };


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1 :
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                } else{
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }

                break;

        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            mDistanceToDestination=route.get(i).getDistanceValue() ;
            mDurationOfTrip =  route.get(i).getDurationValue();

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRoutingCancelled() {

    }


    private float distance ;

    private class detectEndtrip extends Thread{

        @Override
        public void run() {



            if(mLastLocation!=null&&mDestination!=null) {
                distance = mLastLocation.distanceTo(mDestination);

                if (distance<150)
                {
//                    Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_LONG).show();

                    endTrip();
                    erasePolyLines();


                }

            }

        }
     }


    private void endTrip(){


      //  final String[] tripCounter = new String[1];

        //String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("Users").child("Ssf0vJFPNiMiU5snCmhkJfMzZna2").child("Trip");


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               TripCounter= (int) dataSnapshot.getChildrenCount();
               if(TripCounter==0)
               {

                  ref2= userRef.child(String.valueOf(TripCounter));

                   Map map = new HashMap();

                   map.put("Source", myLocationAddressName);
                   map.put("Destination", myDestinationAddressName);
                   map.put("Distance", mDistanceToDestination);
                   map.put("Duration", mDurationOfTrip);

                   ref2.setValue(map);




               }else if(TripCounter>0&&clicked) {


                           ref2=userRef.child(String.valueOf(TripCounter));
                           Map map = new HashMap();
                           map.put("Source", myLocationAddressName);
                           map.put("Destination", myDestinationAddressName);
                           map.put("Distance", mDistanceToDestination);
                           map.put("Duration", mDurationOfTrip);

                           ref2.setValue(map);

                           clicked=false ;


               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void erasePolyLines(){


        for(Polyline line : polylines)
        {
            line.remove();

        }

        polylines.clear();
    }

}
