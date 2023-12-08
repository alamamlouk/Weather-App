package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText searchCity;
    TextView TownAndCityName,weather,wind,temperature,humidity,MoreDetails;
    ImageView weatherImage;
    Button searchBtn;

    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appId = "875c2cc98a63d97585288cf8897e0c27";

    DecimalFormat df = new DecimalFormat("#.##");


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchCity=findViewById(R.id.SearchBarCity);
        TownAndCityName=findViewById(R.id.country_City_Name);
        weather=findViewById(R.id.weatherType);
        wind=findViewById(R.id.WeatherWind);
        temperature=findViewById(R.id.weatherThermometer);
        humidity=findViewById(R.id.weatherHumidity);
        searchBtn=findViewById(R.id.SearchCity);
        weatherImage=findViewById(R.id.weatherImg);
        MoreDetails=findViewById(R.id.ModeWeatherDetails);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempUrl="";
                String city=searchCity.getText().toString().trim();
                if(city.equals("")){
                    Toast.makeText(getApplicationContext(),"Field shouldn't be empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    tempUrl=url+"?q="+city+"&appid="+appId;
                }
                StringRequest stringRequest=new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response", response);
                        String output="";
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                            String description = jsonObjectWeather.getString("description");
                            String mainWeather =jsonObjectWeather.getString("main");
                            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                            double temp = jsonObjectMain.getDouble("temp") - 273.15;
                            int humidityDetail = jsonObjectMain.getInt("humidity");
                            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                            String windSpeed = jsonObjectWind.getString("speed");
                            JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                            JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                            String countryName = jsonObjectSys.getString("country");
                            String cityName = jsonResponse.getString("name");
                            TownAndCityName.setText(countryName+","+cityName);
                            temperature.setText(df.format(temp)+" °C");
                            wind.setText(windSpeed+"m/s");
                            humidity.setText(humidityDetail+"%");
                            weather.setText(mainWeather);
                            MoreDetails.setText(description);
                            switch (mainWeather){
                                case "Clear":
                                    weatherImage.setImageResource(R.drawable.sun);
                                    break;
                                case "Clouds":

                                    weatherImage.setImageResource(R.drawable.cloudy);
                                    break;
                                case "scattered clouds":

                                    weatherImage.setImageResource(R.drawable.cloud);
                                    break;
                                case "broken clouds":

                                    weatherImage.setImageResource(R.drawable.brokencloud);
                                    break;
                                case "shower rain":

                                    weatherImage.setImageResource(R.drawable.rain);
                                    break;
                                case "rain":

                                    weatherImage.setImageResource(R.drawable.car);
                                    break;
                                case "thunderstorm":

                                    weatherImage.setImageResource(R.drawable.thunderstorm);
                                    break;
                                case "snow":

                                    weatherImage.setImageResource(R.drawable.snowflake);
                                    break;
                                case "Mist":
                                    weatherImage.setImageResource(R.drawable.mist);
                                    break;
                                default:
                                    weatherImage.setImageResource(R.drawable.sun);
                                    break;
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            }
        });
    }
}