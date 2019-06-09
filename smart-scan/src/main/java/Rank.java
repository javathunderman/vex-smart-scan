import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.*;


public class Rank {

  public static void main(String[] ignored) throws Exception {
    String skuOriginal = ignored[0];
    if(skuOriginal.equals(null)) {
      System.out.println("u w0t m8");
      System.exit(0);
    }
    URL url = new URL("http://api.vexdb.io/v1/get_rankings?sku="+skuOriginal);
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
      ((Team) teamList.get(i)).setPccvm(false);
      ((Team) teamList.get(i)).setPccvm(true);
      ((Team) teamList.get(i)).setfinalScore();
      //System.out.println(((Team) teamList.get(i)).getTeam() + ": " + ((Team) teamList.get(i)).finalScore());
        i++;
    }
    Collections.sort(teamList, new Output());
    Collections.reverse(teamList);
    i = 0;
    while(i<teamList.size()) {
      System.out.println(((Team) teamList.get(i)).getTeam() + ": " + ((Team) teamList.get(i)).getFinalScore());
      i++;
    }


  }

}
