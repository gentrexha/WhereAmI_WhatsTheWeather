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

public class infoActivity extends AppCompatActivity
{
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
                mAltitude = objJSONObject.getString("elevation");
                // Gets only 3 first chars from elevation
                mAltitude = mAltitude.substring(0, Math.min(mAltitude.length(),3));
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
            // I have to shorten the Lat and Long values because the weather API often doesn't read them
            String mShortLatitudeText = mLatitudeText.substring(0, Math.min(mLatitudeText.length(),5));
            String mShortLongitudeText = mLongitudeText.substring(0, Math.min(mLongitudeText.length(),5));

            StringBuilder urlString = new StringBuilder();
            urlString.append(openweatherAPIURL);
            urlString.append(mShortLatitudeText);
            urlString.append("&lon=");
            urlString.append(mShortLongitudeText);
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
