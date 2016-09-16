package rks.gentrexha.whereamiwhatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class infoActivity extends AppCompatActivity
{
    private String mLatitudeText = "NOSENTVALUE";
    private String mLongitudeText = "NOSENTVALUE";
    private TextView txvLat;
    private TextView txvLong;
    static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?lat=";
    static final String API_KEY = "45031aee347ff5ce623d388389a709a1";
    private String mPlace = "N/A";
    private String mTemp = "N/A";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        txvLat = (TextView)findViewById(R.id.txvLat);
        txvLong = (TextView)findViewById(R.id.txvLong);
        Bundle objBundle = getIntent().getExtras();
        mLatitudeText = objBundle.getString("Lat");
        mLongitudeText = objBundle.getString("Long");
        txvLat.setText(mLatitudeText);
        txvLong.setText(mLongitudeText);
        new RetriveWeather().execute();
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
            urlString.append(API_URL);
            urlString.append(mLatitudeText);
            urlString.append("&lon=");
            urlString.append(mLongitudeText);
            urlString.append("&units=metric");
            urlString.append("&apiKey=");
            urlString.append(API_KEY);

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
                    } catch (IOException ignored) {}
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
                txvLat.setText("THERE WAS AN ERROR!");
            }
            if(result == null)
            {
                txvLat.setText("JSONObject is null!");
            }
            try
            {
                mPlace = result.getString("name");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            txvLat.setText("You are currently in " + mPlace);
        }
    }
}
