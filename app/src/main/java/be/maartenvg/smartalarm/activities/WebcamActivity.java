package be.maartenvg.smartalarm.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;

import be.maartenvg.smartalarm.R;
import be.maartenvg.smartalarm.mjpg.MjpegInputStream;
import be.maartenvg.smartalarm.mjpg.MjpegView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class WebcamActivity extends AppCompatActivity {
    private static final String TAG = "MjpegActivity";
    private SharedPreferences sharedPreferences;

    @Bind(R.id.mjpegview)
    MjpegView mv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webcam);

        ButterKnife.bind(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String streamUrl = sharedPreferences.getString("apiURL", "http://127.0.0.1");
        streamUrl += ":8081";

        new DoRead().execute(streamUrl);
    }

    public void onPause() {
        super.onPause();
        mv.stopPlayback();
    }

    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {
            //TODO: if camera has authentication deal with it and don't just not work
            HttpResponse res = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            Log.d(TAG, "1. Sending http request");
            try {
                res = httpclient.execute(new HttpGet(URI.create(url[0])));
                Log.d(TAG, "2. Request finished, status = " + res.getStatusLine().getStatusCode());
                if(res.getStatusLine().getStatusCode()==401){
                    //You must turn off camera User Access Control before this will work
                    return null;
                }
                return new MjpegInputStream(res.getEntity().getContent());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Log.d(TAG, "Request failed-ClientProtocolException", e);
                //Error connecting to camera
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Request failed-IOException", e);
                //Error connecting to camera
            }

            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            mv.setSource(result);
            mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            mv.showFps(true);
        }
    }
}
