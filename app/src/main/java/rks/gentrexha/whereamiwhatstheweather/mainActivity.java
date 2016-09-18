package rks.gentrexha.whereamiwhatstheweather;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkGPSStatus();
    }

    public void btnMoreOnClick(View v)
    {
        Button btnMore = (Button)findViewById(R.id.btnMore);
        Intent intMap = new Intent(this, mapActivity.class);
        startActivity(intMap);
    }

    public void btnHistoryOnClick(View v)
    {
        Button btnHistory = (Button)findViewById(R.id.btnHistory);
        Intent intHistory = new Intent(this, historyActivity.class);
        startActivity(intHistory);
    }

    private void checkGPSStatus()
    {
        LocationManager locationManager = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        if ( locationManager == null )
        {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        try
        {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch (Exception ignored)
        {
        }
        try
        {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch (Exception ignored)
        {
        }
        if ( !gps_enabled && !network_enabled )
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity.this);
            dialog.setMessage("Your GPS is disabled, for this application to work it has to be enabled!");
            dialog.setPositiveButton("Enable", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    // This will navigate user to the device location settings screen
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
        }
    }
}
