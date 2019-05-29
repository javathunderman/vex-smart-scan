import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;


public class Rank {

  public static void main(String[] ignored) throws Exception {

    URL url = new URL("http://api.vexdb.io/v1/get_rankings?sku=RE-VRC-18-7443");
    InputStreamReader reader = new InputStreamReader(url.openStream());
    List teamList = new ArrayList<Team>();
    JsonParser jsonParser = new JsonParser();
    JsonArray results = jsonParser.parse(reader)
            .getAsJsonObject().getAsJsonArray("result");
    Gson gson = new Gson();
    for (JsonElement result : results) {
        teamList.add(gson.fromJson(result, Team.class));
    }
    Team temp =  (Team)teamList.get(0); //wtf java
    Event currentEvent = new Event(temp.getSku());
    currentEvent.setSeason();
    String season = (currentEvent.getSeason());
    int i = 0;
    while(i<teamList.size()) {
      ((Team) teamList.get(i)).setAwards(season);
      ((Team) teamList.get(i)).setSkills();
        i++;
    }
    System.out.println(teamList);
  }

}