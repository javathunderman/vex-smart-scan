import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.*;


public class Rank {

  public static void main(String[] ignored) throws Exception {

    URL url = new URL("http://api.vexdb.io/v1/get_rankings?sku=RE-VRC-18-7443");
    InputStreamReader reader = new InputStreamReader(url.openStream());
    ArrayList teamList = new ArrayList<Team>();
    JsonParser jsonParser = new JsonParser();
    JsonArray results = jsonParser.parse(reader)
            .getAsJsonObject().getAsJsonArray("result");
    Gson gson = new Gson();
    for (JsonElement result : results) {
        teamList.add(gson.fromJson(result, Team.class));
    }

    System.out.println(teamList);
    Team temp =  (Team)teamList.get(0); //wtf java
    Event currentEvent = new Event(temp.getSku());
    currentEvent.setSeason();
    System.out.println(currentEvent);
    temp.setAwards(currentEvent.getSeason());
  }

}