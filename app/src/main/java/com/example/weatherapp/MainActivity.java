package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{

    //opzet van componenten
    TextView temper,city, unit, date, weatherDescription;
    ImageView icon;
    EditText searchbar;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //componenten linken
        temper = findViewById(R.id.temper);
        unit = findViewById(R.id.temper2);
        city = findViewById(R.id.city);
        icon = findViewById(R.id.icon);
        searchbar = findViewById(R.id.searchbar);
        button = findViewById(R.id.button);
        date = findViewById(R.id.date);


        //functie van huidige datum ophalen word hier geactiveerd
        date.setText(getCurrentDate());

        //deze components zijn invisible en weer visible wanneer go functie word gebruikt.
        temper.setVisibility(View.INVISIBLE);
        city.setVisibility(View.INVISIBLE);
        unit.setVisibility(View.INVISIBLE);

    }

    //function huidige datum ophalen
    private String getCurrentDate ()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        String formattedDate = dateFormat.format(calendar.getTime());

        return formattedDate;
    }

    //na button click gebeurt dit (fetch data van api)
    public void go(View view)
    {
        String cityy = searchbar.getText().toString().trim();
        //url voor api
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityy + "&appid=6d79d325fe8b557fe610609a7c139384&units=metric";

        //components worden nu visible = temperature, city en unit
        temper.setVisibility(View.VISIBLE);
        city.setVisibility(View.VISIBLE);
        unit.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseObject) {
                        temper.setText("Response: " + responseObject.toString());
                        city.setText(cityy);
                        //kale/lelijke informatie format van de json
                        Log.v("WEATHER", "Response: " + responseObject.toString());

                        try
                        {
                            JSONObject mainObject = responseObject.getJSONObject("main");

                            //integer word afgerond zodat geen cijfers achter komma word laten zien
                            String temp = Integer.toString((int) Math.round(mainObject.getDouble("temp")));
                            double grade = mainObject.getDouble("temp");

                            //icon veranderd op graden verschil, getal 15 is het punt
                            if (grade > 15 ){
                                icon.setImageResource(R.drawable.sun);
                            }
                            if (grade < 15){
                                icon.setImageResource(R.drawable.rainy);
                            }

                            temper.setText(temp);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handel error met toast bericht
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

        // zorgt ervoor dat de requests op een rij worden gezet en daarna,
        // uitgevoerd voor de json object
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}