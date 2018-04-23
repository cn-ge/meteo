package cgeindreau2015.ca.meteo.models;

import java.security.PrivilegedAction;
import java.util.List;

/**
 * Created by cgeindreau2015 on 27/04/2017.
 */

public class OpenWeather {

    public String name;
    public String cod;  // mettre un string si code erreur

    public String message; // si ville inconnue

    public OpenWeatherMain main;
    public List<OpenWeatherWeather> weather;
    public OpenWeatherCoord coord;
}

/*http://api.openweathermap.org/data/2.5/weather?q=Athens,gr&units=metric&appid=848e5c50c3ed1a0f0b1ffdecc8622dfa
{
        coord: {
            lon: 23.72,
            lat: 37.98
        },
        weather: [
            {
                id: 800,
                main: "Clear",
                description: "clear sky",
                icon: "01d"
            }
            {
                id: 800,
                main: "Cloudy",
                description: "broken sky",
                icon: "04d"
            }
        ],
        base: "stations",
        main: {
            temp: 24.45,
            pressure: 1016,
            humidity: 31,
            temp_min: 24,
            temp_max: 25
        },
        visibility: 10000,
        wind: {
            speed: 4.1,
            deg: 180
        },
        clouds: {
            all: 0
        },
        dt: 1493297400,
        sys: {
            type: 1,
            id: 5675,
            message: 0.1247,
            country: "GR",
            sunrise: 1493263991,
            sunset: 1493313164
        },
        id: 264371,
        name: "Athens",
        cod: 200
    }
*/