package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView resultTextview;

    public void getWeather(View view){
        DownloadTask task = new DownloadTask();
        try {
            String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");


            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName +"&appid=bcbf4d9df5b112f893d08e1bd1c3b197");
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(),"could not find weather :(",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in =urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!=-1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch(Exception e){

                Toast.makeText(getApplicationContext(),"could not find weather :(",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("weather content",weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);
                String message = "";
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonPart= arr.getJSONObject(i);

                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");
                    if(!main.equals("") && !description.equals("")){
                        message += main + " : " + description + "\r\n";
                    }


                }
                if(!message.equals("")){
                    Log.i("info", message);
                    resultTextview.setText(message);
                }
                else{
                    Toast.makeText(getApplicationContext(),"could not find weather :(",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e){

                Toast.makeText(getApplicationContext(),"could not find weather :(",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        resultTextview = findViewById(R.id.resultTextView);



    }
}