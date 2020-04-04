import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Event {
    private String sku;
    private String datetime;
    private String season;
    public Event(String sku) {
        this.sku = sku;
        String season;
        String datetime;
    }
    public void setSeason() throws IOException {

        URL getEvents = new URL("http://api.vexdb.io/v1/get_events?sku=" + sku);
        InputStreamReader eventsReader = new InputStreamReader(getEvents.openStream());
        JsonParser jsonParser = new JsonParser();
        JsonArray results = (JsonArray) jsonParser.parse(eventsReader).getAsJsonObject().get("result"); //idk what the hell this is
        JsonObject result =  new Gson().fromJson(results.get(0), JsonObject.class);
        this.season = java.net.URLEncoder.encode((result.get("season").getAsString()), "UTF-8").replace(" ", "%20");
        this.datetime = java.net.URLEncoder.encode((result.get("start").getAsString()), "UTF-8").replace(" ", "%20");
    }
    public String getSku() {
        return sku;
    }
    public String getSeason() {
        return season;
    }
    public String getDatetime() {
        return datetime;
    }
    public String toString() {
        return("SKU: " + sku + "\nSeason: " + season + "\nDate: " + datetime);
    }
    public String getPreviousSeason(boolean twoyears) {
        String [] seasonList = {"Starstruck", "In+The+Zone", "Turning+Point", "Tower+Takeover"};
        int index = -1;
        for(int i = 0; i<seasonList.length; i++) {
            if(seasonList[i].equals(season)) {
                index = i;
                break;
            }
        }
        if(index-1 < 0) {
            return("No season data found");
        }
        if(twoyears) {
            return seasonList[index-2];
        }
        else {
            return seasonList[index-1];
        }

    }
}

