import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

public class PreviousSeasons {
    public static String getOldSKU(String team, String sku, boolean twoyears) throws IOException {
        ArrayList<String> previousdates = new ArrayList<>();
        ArrayList<Date> previousDateList = new ArrayList<>();
        Event eventThing = new Event(sku);
        eventThing.setSeason();
        Date currentDate = null;
        String currentDateString = eventThing.getDatetime();
        currentDateString = currentDateString.replace("%3A", "");
        currentDateString = currentDateString.substring(0, currentDateString.indexOf("T"));
        try
        {
            DateFormat formatter;
            formatter = new SimpleDateFormat("yy-MM-dd");
            currentDate = ((Date)formatter.parse(currentDateString));

        }
        catch (Exception e)
        {}
        URL url = new URL("https://api.vexdb.io/v1/get_events?season=" + eventThing.getPreviousSeason(twoyears) + "&team=" + team);
        
        InputStreamReader reader = new InputStreamReader(url.openStream());
        JsonParser jsonParser = new JsonParser();
        JsonArray results = (JsonArray) jsonParser.parse(reader).getAsJsonObject().get("result"); //idk what the hell this is

        for(JsonElement result: results) {
            JsonObject temp = result.getAsJsonObject();
            String stuff = String.valueOf(temp.get("sku"));
            stuff = stuff.replace("\"", "");
            previousdates.add(stuff);

        }
        
        for(int i = 0; i<previousdates.size(); i++) {
            Event newEvent = new Event(previousdates.get(i));
            newEvent.setSeason();
            String time = newEvent.getDatetime();
            time = time.replace("%3A", "");
            time = time.substring(0, time.indexOf("T"));
            
            try
            {
                DateFormat formatter;
                formatter = new SimpleDateFormat("yy-MM-dd");
                previousDateList.add((Date)formatter.parse(time));

            }
            catch (Exception e)
            {}

        }

        
        
        Date nearest = (getDateNearest(previousDateList, currentDate));
        
        int indexSKUlookup = previousDateList.indexOf(nearest);
        return(previousdates.get(indexSKUlookup));
    }
    public static double setPCCWM(String team, String sku, boolean twoyears) throws IOException {
        String oldSKU = getOldSKU(team, sku, twoyears);
        JsonParser jsonParser = new JsonParser();
        URL url2 = new URL("https://api.vexdb.io/v1/get_rankings?team="+team + "&sku="+oldSKU);
        
        InputStreamReader reader2 = new InputStreamReader(url2.openStream());

        try {
            JsonArray stuff2 = (JsonArray) jsonParser.parse(reader2).getAsJsonObject().get("result"); //idk what the hell this is
            
            return(stuff2.get(0).getAsJsonObject().get("ccwm").getAsDouble());

        }
        catch (Exception e) {
            return(0.0);
        }
    }
    public static int setPWASP(String team, String sku, String option) throws IOException {
        String oldSKU = getOldSKU(team, sku, false);
        JsonParser jsonParser = new JsonParser();
        URL url2 = new URL("https://api.vexdb.io/v1/get_rankings?team="+team + "&sku="+oldSKU);
        
        InputStreamReader reader2 = new InputStreamReader(url2.openStream());

        try {
            JsonArray stuff2 = (JsonArray) jsonParser.parse(reader2).getAsJsonObject().get("result"); //idk what the hell this is
            if(option.equals("wp")) {
                return(stuff2.get(0).getAsJsonObject().get("wp").getAsInt());
            }
            else if(option.equals("ap")) {
                return(stuff2.get(0).getAsJsonObject().get("ap").getAsInt());
            }
            else if(option.equals("sp")) {
                return(stuff2.get(0).getAsJsonObject().get("sp").getAsInt());
            }
            else {
                return(0);
            }

        }
        catch (Exception e) {
            return(0);
        }
    }

    private static Date getDateNearest(List<Date> dates, Date targetDate){
        return new TreeSet<Date>(dates).lower(targetDate);
    }

}
