package rks.gentrexha.whereamiwhatstheweather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class mapActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    private GoogleMap mMap;
    //
    private GoogleApiClient mGoogleApiClient;
    //
    private Location mLastLocation;
    private Location mCurrentLocation;
    // Variables where information is stored
    private String mLatitudeText = "n/a";
    private String mLongitudeText = "n/a";
    private String mAltitude = "n/a";
    // Bundle to pass variables to next activity
    private Bundle objBundle = new Bundle();
    // Have to find out about this one
    private int locationRequestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Asks for permission to use location services
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},locationRequestCode);
            // locationRequestCode is an app-defined int constant. The callback method gets the result of the request.
        }

        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Builds the Google API Client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        // Asks for permission to use location services
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},locationRequestCode);
            // locationRequestCode is an app-defined int constant. The callback method gets the result of the request.
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onStart()
    {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        // Asks for permission to use location services
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},locationRequestCode);
            // locationRequestCode is an app-defined int constant. The callback method gets the result of the request.
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
        {
            // Moves Camera to latest location and assigns values to variables for the next activity
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
            mAltitude = String.valueOf(mLastLocation.getAltitude());
            LatLng latLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        }
    }
    public void btnInfoOnClick(View v)
    {
        Intent intInfo = new Intent(this, infoActivity.class);
        // Passing values to bundle for next activity
        objBundle.putString("Lat",mLatitudeText);
        objBundle.putString("Long",mLongitudeText);
        objBundle.putString("Alt",mAltitude);
        intInfo.putExtras(objBundle);
        startActivity(intInfo);
    }

    @Override
    public void onConnectionSuspended(int i)
    {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
    }

    @Override
    public void onLocationChanged(Location location)
    {
    }
}
