package cgeindreau2015.ca.meteo.models;

import java.util.StringTokenizer;

/**
 * Created by cgeindreau2015 on 27/04/2017.
 */

public class OpenWeatherMain {

    private String temp;

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp() {
        return temp;
        /*
        StringTokenizer tempSplit = new StringTokenizer(temp, ".");
        String first = tempSplit.nextToken();
        return String.valueOf(first);
        */
    }


}
