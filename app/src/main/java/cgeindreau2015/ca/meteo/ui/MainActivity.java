package cgeindreau2015.ca.meteo.ui;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshHorizontalScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.internal.Utils;
import com.squareup.picasso.Picasso;

import java.util.StringTokenizer;

import cgeindreau2015.ca.meteo.R;
import cgeindreau2015.ca.meteo.models.OpenWeather;
import cgeindreau2015.ca.meteo.util.Constant;
import cgeindreau2015.ca.meteo.util.FastDialog;
import cgeindreau2015.ca.meteo.util.Network;
import cgeindreau2015.ca.meteo.util.Preferences;

public class MainActivity extends AppCompatActivity {

    private EditText edit_Ville;
    private TextView text_Ville;
    private TextView text_Degre;
    private TextView text_Info;
    private Button btn_Valider;
    private ImageView image_Meteo;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ScrollView mScrollView;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_Ville = (EditText) findViewById(R.id.edit_ville);
        edit_Ville.setText(Preferences.getCity(MainActivity.this));
        text_Ville = (TextView) findViewById(R.id.text_ville);
        text_Degre = (TextView) findViewById(R.id.text_degre);
        text_Info = (TextView) findViewById(R.id.text_info);
        btn_Valider = (Button) findViewById(R.id.btn_valider);
        image_Meteo = (ImageView) findViewById(R.id.image_meteo);

        //METHODE RECUPEREE DU MODULE IMPORTEE DE APP-PULL-TO-REFRESH
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                btn_Valider.performClick();
            }

        });

        if(!Network.isNetworkAvailable(MainActivity.this)) {
            if (Preferences.getOpenWeather(MainActivity.this) != null) {
                AfficherMeteo(Preferences.getOpenWeather(MainActivity.this));
                String maj = Preferences.getLastUpdate(MainActivity.this);
                text_Info.setText("Dernière MAJ le "+ maj);
                text_Info.setVisibility(View.VISIBLE);
            }
        } else  {
            text_Info.setVisibility(View.GONE);
            btn_Valider.performClick();
        }
        mScrollView = mPullRefreshScrollView.getRefreshableView();
    }

    private void AfficherMeteo(OpenWeather openWeather) {
        text_Ville.setText(openWeather.name);
        text_Degre.setText(openWeather.main.getTemp() + " °C");
        if (openWeather.weather.size() > 0) {
            String icon = String.format(Constant.URL_IMAGE, openWeather.weather.get(0).icon);
            Picasso.with(MainActivity.this).load(icon).into(image_Meteo);
        }
    }

    public void Valider(View view) {
        if (!edit_Ville.getText().toString().isEmpty())
        {   // verification du mode connecté OU avion
            if(Network.isNetworkAvailable(MainActivity.this)) {

                //http://api.openweathermap.org/data/2.5/weather?q=NewYork,us&units=metric&appid=848e5c50c3ed1a0f0b1ffdecc8622dfa
                //aller sur Volleyandroid : https://developer.android.com/training/volley/simple.html


                // Affichage du message d'attente
                dialog = FastDialog.showProgressDialog(MainActivity.this, "Chargement ...");
                dialog.show();

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url = String.format(Constant.URL, edit_Ville.getText().toString());
                Log.v("url", url);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                //http://api.openweathermap.org/data/2.5/weather?q=NewYork,us&units=metric&appid=848e5c50c3ed1a0f0b1ffdecc8622dfa

                                // Conversion du Json en Objets
                                Gson gson = new Gson();
                                OpenWeather openWeather = gson.fromJson(response, OpenWeather.class);

                                if (openWeather.cod.equals("200")) {
                                    AfficherMeteo(openWeather);
                                    Preferences.setCity(MainActivity.this, edit_Ville.getText().toString().toUpperCase());
                                    Preferences.setOpenWeather(MainActivity.this, openWeather);
                                    mPullRefreshScrollView.onRefreshComplete();
                                }
                                dialog.dismiss();
                            }
                        }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    mPullRefreshScrollView.onRefreshComplete();
                                    dialog.dismiss();
                                }
                });
                queue.add(stringRequest);
                text_Info.setVisibility(View.GONE);
            } else {
                mPullRefreshScrollView.onRefreshComplete();
                FastDialog.showDialog(MainActivity.this, FastDialog.PROGRESS_DIALOG, getString(R.string.main_error_network_down));
            }
        } else {
            FastDialog.showDialog(MainActivity.this, FastDialog.SIMPLE_DIALOG, getString(R.string.main_error_city_empty));
            text_Info.setVisibility(View.GONE);
        }
    }

    public void SeeMap(View view) {
        /*
        Intent i = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(i);
        */
        Intent i = new Intent(MainActivity.this, MapsActivity.class);
        OpenWeather openWeather = Preferences.getOpenWeather(getApplicationContext());
        i.putExtra("Lat", openWeather.coord.lat);
        i.putExtra("Lon", openWeather.coord.lon);
        i.putExtra("City", text_Ville.getText().toString());
        startActivity(i);
    }
}
