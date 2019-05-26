import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Event {
    private String sku;
    private String season;
    public Event(String sku) {
        this.sku = sku;
        String season;

    }
    public void setSeason() throws IOException {

        URL url = new URL("http://api.vexdb.io/v1/get_events?sku=" + sku);
        InputStreamReader reader = new InputStreamReader(url.openStream());
        JsonParser jsonParser = new JsonParser();
        JsonArray results = (JsonArray) jsonParser.parse(reader).getAsJsonObject().get("result"); //idk what the hell this is
        JsonObject result =  new Gson().fromJson(results.get(0), JsonObject.class);
        this.season = java.net.URLEncoder.encode((result.get("season").getAsString()), "UTF-8").replace(" ", "%20");
    }
    public String getSku() {
        return sku;
    }
    public String getSeason() {
        return season;
    }
    public String toString() {
        return("SKU: " + sku + "\nSeason: " + season);
    }
}

