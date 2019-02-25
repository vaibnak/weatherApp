package com.example.user.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.myEdittext);
        button = (Button)findViewById(R.id.myButton);
        textView = (TextView)findViewById(R.id.mytextView);

    }

    public void doSearch(View view) {
        String city = editText.getText().toString();
        String search = "http://api.openweathermap.org/data/2.5//weather?q="+city+"&APPID=85f808e3a136177ee86bafc9ab2211f2";
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(search);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL(urls[0]);
                String result = "";
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1){
                    char current = (char)data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatheInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatheInfo);
                JSONObject jobj = arr.getJSONObject(0);

                String u = jsonObject.getString("main");
                JSONObject ju = new JSONObject(u);
                String info = ju.getString("temp");
                String fn = "Temperature: "+ju.getString("temp")+"\nHumdity: "+ju.getString("humidity")+"\nPressure: "+ju.getString("pressure")+"\nDescription: "+jobj.getString("description");
                textView.setText(fn);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
