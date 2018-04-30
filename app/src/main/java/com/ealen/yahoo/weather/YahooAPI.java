package com.ealen.yahoo.weather;

import android.os.StrictMode;

import com.ealen.yahoo.weather.forecast.Astronomy;
import com.ealen.yahoo.weather.forecast.Atmosphere;
import com.ealen.yahoo.weather.forecast.Condition;
import com.ealen.yahoo.weather.forecast.Forecast;
import com.ealen.yahoo.weather.forecast.Location;
import com.ealen.yahoo.weather.forecast.Wind;
import com.ealen.yahoo.weather.forecast.YahooForecastResponse;
import com.ealen.yahoo.weather.places.YahooPlacesResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ealen on 18/03/2016.
 */
public class YahooAPI {
    private YahooPlacesResponse _Places;
    private Astronomy _Astronomy;
    private Atmosphere _Atmosphere;
    private Wind _Wind;
    private Condition _Condition;
    private Location _Location;

    private List<Forecast> _ListForecast;
    private String _strCity= "";

    /**
     * Create manager of yahoo weather
     * @param strCity Name of city with state
     */
    public YahooAPI(String strCity){
        // For url
        _strCity = strCity;
        _strCity = StringOperation.withoutAccents(_strCity);
        _strCity = _strCity.replace(" ", "%20");

        // Set
        _Astronomy = new Astronomy();
        _Atmosphere = new Atmosphere();
        _Wind = new Wind();
        _Condition = new Condition();
        _Location = new Location();
        _ListForecast = new ArrayList<Forecast>();
    }

    /**
     * Sync data to this object with internet (Yahoo.com)
     * @throws IOException
     */
    public void syncData() throws IOException{
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Get Place
        String url_json_woeid = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text%3D%22" + _strCity + "%22&format=json";
        String json_woeid = getJSON(url_json_woeid);
        _Places = gson.fromJson(json_woeid, YahooPlacesResponse.class);

        // Get Weather
        String url_json_weather = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20%3D%20" +
                _Places.getQuery().getResults().getPlace().getWoeid() +
                "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        String json_weather =getJSON(url_json_weather);

        YahooForecastResponse ForecastResponse = gson.fromJson(json_weather, YahooForecastResponse.class);

        // Set
        if(ForecastResponse.getQuery() != null) {
            _Astronomy = ForecastResponse.getQuery().getResults().getChannel().getAstronomy();
            _Atmosphere = ForecastResponse.getQuery().getResults().getChannel().getAtmosphere();
            _Wind = ForecastResponse.getQuery().getResults().getChannel().getWind();
            _Condition = ForecastResponse.getQuery().getResults().getChannel().getItem().getCondition();
            _Location = ForecastResponse.getQuery().getResults().getChannel().getLocation();
            _ListForecast = ForecastResponse.getQuery().getResults().getChannel().getItem().getForecast();
        }
    }


    /* -----------------------------------------------------------
     * Getter
     * -----------------------------------------------------------
     */

    public Astronomy Astronomy() {
        return _Astronomy;
    }

    public Atmosphere Atmosphere() {
        return _Atmosphere;
    }

    public Wind Wind() {
        return _Wind;
    }

    public Condition Condition() {
        return _Condition;
    }

    public Location Location() {
        return _Location;
    }

    public List<Forecast> ListForecast() {
        return _ListForecast;
    }

    /**
     * Get Json (Webservice) for URL
     * @param iURL URL of web service
     * @return Response of web page
     * @throws MalformedURLException
     * @throws IOException
     */
    private String getJSON(String iURL) throws IOException{
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(iURL)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
