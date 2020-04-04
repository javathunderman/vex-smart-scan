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
    public static String getOldSKU(String sku, String team, boolean twoyears) throws IOException {
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
        {
            System.out.println("Fatal exception, PCCWM date format");
        }
        URL getEvents = new URL("https://api.vexdb.io/v1/get_events?season=" + eventThing.getPreviousSeason(twoyears) + "&team=" + team);
        InputStreamReader eventsReader = new InputStreamReader(getEvents.openStream());
        JsonParser jsonParser = new JsonParser();
        JsonArray results = (JsonArray) jsonParser.parse(eventsReader).getAsJsonObject().get("result");

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
            {
                System.out.println("Fatal exception, PCCWM previous date format");
            }

        }
        Date nearest = (getDateNearest(previousDateList, currentDate));
        int indexSKUlookup = previousDateList.indexOf(nearest);
        return(previousdates.get(indexSKUlookup));
    }
    public static double setPCCWM(String team, String oldSKU) throws IOException {
        JsonParser jsonParser = new JsonParser();
        URL oldSKUURL = new URL("https://api.vexdb.io/v1/get_rankings?team="+ team + "&sku="+oldSKU);
        InputStreamReader reader2 = new InputStreamReader(oldSKUURL.openStream());

        try {
            JsonArray stuff2 = (JsonArray) jsonParser.parse(reader2).getAsJsonObject().get("result");
            return(stuff2.get(0).getAsJsonObject().get("ccwm").getAsDouble());

        }
        catch (Exception e) {
            return(0.0);
        }
    }
    public static int setPWASP(String team, String oldSKU, String option) throws IOException {
        URL url2 = new URL("https://api.vexdb.io/v1/get_rankings?team="+team + "&sku="+oldSKU);
        JsonParser jsonParser = new JsonParser();
        InputStreamReader reader2 = new InputStreamReader(url2.openStream());
        try {
            JsonArray stuff2 = (JsonArray) jsonParser.parse(reader2).getAsJsonObject().get("result"); //idk what the hell this is
            int result = 0;
            if(option.equals("wp"))
                result = (stuff2.get(0).getAsJsonObject().get("wp").getAsInt());
            else if(option.equals("ap"))
                result = (stuff2.get(0).getAsJsonObject().get("ap").getAsInt());
            else if(option.equals("sp"))
                result = (stuff2.get(0).getAsJsonObject().get("sp").getAsInt());
            return result;
        }
        catch (Exception e) {
            return(0);
        }
    }
    private static Date getDateNearest(List<Date> dates, Date targetDate){
        return new TreeSet<Date>(dates).lower(targetDate);
    }
}
