package rks.gentrexha.whereamiwhatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

// References:
// http://stackoverflow.com/questions/19050294/what-is-the-most-efficient-way-on-android-to-call-http-web-api-calls-that-return
// http://www.androidauthority.com/use-remote-web-api-within-android-app-617869/
// http://www.tutorialspoint.com/android/android_json_parser.htm
// https://developers.google.com/maps/documentation/elevation/intro

public class infoActivity extends AppCompatActivity
{
    // Variables which get their values from the bundle
    private String mLatitudeText = "n/a";
    private String mLongitudeText = "n/a";
    private String mAltitude = "n/a";
    private TextView txvInfo;
    // OpenWeather API
    static final String openweatherAPIURL = "http://api.openweathermap.org/data/2.5/weather?lat=";
    static final String openweatherAPIKey = "45031aee347ff5ce623d388389a709a1";
    // GoogleMaps Elevation API
    static final String elevationAPIURUL = "https://maps.googleapis.com/maps/api/elevation/json?locations=";
    static final String elevationAPIKey = "AIzaSyBvs_wbNGAqayC9V5sApoDzCi0gwfYszlQ";
    private String mPlace = "n/a";
    private String mTemp = "n/a";
    DBHelper objDB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        txvInfo = (TextView)findViewById(R.id.txvLat);
        objDB = new DBHelper(this);

        Bundle objBundle = getIntent().getExtras();
        mLatitudeText = objBundle.getString("Lat");
        mLongitudeText = objBundle.getString("Long");

        new RetrieveElevation().execute();
        new RetriveWeather().execute();
    }

    class RetrieveElevation extends AsyncTask<String,Void,JSONObject>
    {
        Exception mException = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            this.mException = null;
        }

        @Override
        protected JSONObject doInBackground(String... strings)
        {
            StringBuilder urlString = new StringBuilder();
            urlString.append(elevationAPIURUL);
            urlString.append(mLatitudeText);
            urlString.append(",");
            urlString.append(mLongitudeText);
            urlString.append("&key=");
            urlString.append(elevationAPIKey);

            Log.println(Log.INFO,"ELEVATION URL",urlString.toString());

            HttpURLConnection objURLConnection = null;
            URL objURL = null;
            JSONObject objJSON = null;
            InputStream objInStream = null;

            try
            {
                objURL = new URL(urlString.toString());
                objURLConnection = (HttpURLConnection) objURL.openConnection();
                objURLConnection.setRequestMethod("GET");
                objURLConnection.setDoOutput(true);
                objURLConnection.setDoInput(true);
                objURLConnection.connect();
                objInStream = objURLConnection.getInputStream();
                BufferedReader objBReader = new BufferedReader(new InputStreamReader(objInStream));
                String line = "";
                String response = "";
                while ((line = objBReader.readLine()) != null)
                {
                    response += line;
                }
                objJSON = (JSONObject) new JSONTokener(response).nextValue();
            }
            catch (Exception e)
            {
                this.mException = e;
            }
            finally
            {
                if (objInStream != null)
                {
                    try
                    {
                        objInStream.close(); // this will close the bReader as well
                    }
                    catch (IOException ignored)
                    {
                    }
                }
                if (objURLConnection != null)
                {
                    objURLConnection.disconnect();
                }
            }
            return (objJSON);
        }

        @Override
        protected void onPostExecute(JSONObject result)
        {
            super.onPostExecute(result);
            try
            {
                JSONArray objJSONArray = result.optJSONArray("results");
                JSONObject objJSONObject = objJSONArray.getJSONObject(0);
                int intAltitude = objJSONObject.getInt("elevation");
                mAltitude = Integer.toString(intAltitude);
                Log.println(Log.INFO,"JSON",mAltitude);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    class RetriveWeather extends AsyncTask<String, Void, JSONObject>
    {
        Exception mException = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            this.mException = null;
        }

        @Override
        protected JSONObject doInBackground(String... strings)
        {
            StringBuilder urlString = new StringBuilder();
            urlString.append(openweatherAPIURL);
            urlString.append(mLatitudeText);
            urlString.append("&lon=");
            urlString.append(mLongitudeText);
            urlString.append("&units=metric");
            urlString.append("&apiKey=");
            urlString.append(openweatherAPIKey);

            Log.println(Log.INFO,"URL:",urlString.toString());

            HttpURLConnection objURLConnection = null;
            URL objURL = null;
            JSONObject objJSON = null;
            InputStream objInStream = null;

            try
            {
                objURL = new URL(urlString.toString());
                objURLConnection = (HttpURLConnection) objURL.openConnection();
                objURLConnection.setRequestMethod("GET");
                objURLConnection.setDoOutput(true);
                objURLConnection.setDoInput(true);
                objURLConnection.connect();
                objInStream = objURLConnection.getInputStream();
                BufferedReader objBReader = new BufferedReader(new InputStreamReader(objInStream));
                String line = "";
                String response = "";
                while ((line = objBReader.readLine()) != null)
                {
                    response += line;
                }
                objJSON = (JSONObject) new JSONTokener(response).nextValue();
            }
            catch (Exception e)
            {
                this.mException = e;
            }
            finally
            {
                if (objInStream != null)
                {
                    try
                    {
                        objInStream.close(); // this will close the bReader as well
                    }
                    catch (IOException ignored)
                    {
                    }
                }
                if (objURLConnection != null)
                {
                    objURLConnection.disconnect();
                }
            }
            return (objJSON);
        }

        @Override
        protected void onPostExecute(JSONObject result)
        {
            super.onPostExecute(result);
            if (this.mException != null)
            {
                txvInfo.setText("THERE WAS AN ERROR!");
            }
            if(result == null)
            {
                txvInfo.setText("JSONObject is null!");
            }
            try
            {
                mPlace = result.getString("name");
                JSONObject main = result.getJSONObject("main");
                mTemp = main.getString("temp");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            txvInfo.setText("You are currently in " + mPlace + ", currently "+ mAltitude +" meters above sea level, "
            + "and the local temperature is "+mTemp+" degrees celsius.");
            mTemp += " " + (char) 0x2103;
            objDB.insertLocation(mPlace,mTemp,new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));
        }
    }
}
