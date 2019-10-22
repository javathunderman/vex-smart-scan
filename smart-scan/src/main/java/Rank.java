import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.*;


public class Rank {

  public static void main(String[] ignored) throws Exception {
    String skuOriginal = ignored[0];
    String regexCheck = "^RE-VRC-(17|18|19|)-(\\d\\d\\d\\d)";
    Pattern r = Pattern.compile(regexCheck);
    Matcher m = r.matcher(skuOriginal);
    if (skuOriginal.equals(null)) {
      System.out.println("u w0t m8");
      System.exit(0);
    }
    if (skuOriginal.equals("rundebug")) {
      System.out.println("[{\"name\":\"4001A\",\"score\":194},{\"name\":\"8637A\",\"score\":139},{\"name\":\"729M\",\"score\":119},{\"name\":\"4001F\",\"score\":99},{\"name\":\"242A\",\"score\":79},{\"name\":\"242Z\",\"score\":70},{\"name\":\"242F\",\"score\":65},{\"name\":\"9071B\",\"score\":50},{\"name\":\"242B\",\"score\":43},{\"name\":\"4017A\",\"score\":33},{\"name\":\"242D\",\"score\":31},{\"name\":\"19030P\",\"score\":28},{\"name\":\"9071A\",\"score\":20},{\"name\":\"9071C\",\"score\":15},{\"name\":\"19030C\",\"score\":14},{\"name\":\"9071X\",\"score\":9},{\"name\":\"242C\",\"score\":4},{\"name\":\"4001E\",\"score\":2},{\"name\":\"9071Y\",\"score\":-4},{\"name\":\"19030A\",\"score\":-6},{\"name\":\"242E\",\"score\":-18},{\"name\":\"4017B\",\"score\":-21}]");
      System.exit(0);
    }
    if (!m.find()) {
      System.out.println("Not a valid SKU");
      System.exit(0);
    }

    try {
      URL url = new URL("http://api.vexdb.io/v1/get_rankings?sku=" + skuOriginal);
      InputStreamReader reader = new InputStreamReader(url.openStream());
      List teamList = new ArrayList<Team>();
      List resultList = new ArrayList<Result>();
      JsonParser jsonParser = new JsonParser();
      JsonArray results = jsonParser.parse(reader)
              .getAsJsonObject().getAsJsonArray("result");
      Gson gson = new Gson();
      for (JsonElement result : results) {
        teamList.add(gson.fromJson(result, Team.class));
      }
      Team temp = (Team) teamList.get(0); //wtf java
      Event currentEvent = new Event(temp.getSku());
      currentEvent.setSeason();
      String season = (currentEvent.getSeason());
      int i = 0;
      while (i < teamList.size()) {
        ((Team) teamList.get(i)).setAwards(season);
        ((Team) teamList.get(i)).setSkills();
        ((Team) teamList.get(i)).setPccvm(false);
        ((Team) teamList.get(i)).setPccvm(true);
        ((Team) teamList.get(i)).setfinalScore();
        //System.out.println(((Team) teamList.get(i)).getTeam() + ": " + ((Team) teamList.get(i)).finalScore());
        i++;
      }
      //System.out.println(temp);
      Collections.sort(teamList, new Output());
      Collections.reverse(teamList);
      i = 0;
      while (i < teamList.size()) {
        resultList.add(new Result(((Team) teamList.get(i)).getTeam(), ((Team) teamList.get(i)).getFinalScore()));
        i++;
      }
      String finalJson = new Gson().toJson(resultList);
      System.out.println(finalJson);
    }
    catch (Exception IndexOutOfBoundsException) {
      URL url = new URL("http://api.vexdb.io/v1/get_teams?sku=" + skuOriginal);
      InputStreamReader reader = new InputStreamReader(url.openStream());
      List teamList = new ArrayList<Team>();
      List resultList = new ArrayList<Result>();
      JsonParser jsonParser = new JsonParser();
      JsonArray results = jsonParser.parse(reader)
              .getAsJsonObject().getAsJsonArray("result");
      Gson gson = new Gson();
      //System.out.println(results);
      for (JsonElement result : results) {
        teamList.add(gson.fromJson(result, Team.class));
        //System.out.println(result["number"]);
      }
      //System.out.println(temp);
      Event currentEvent = new Event(skuOriginal);
      //System.out.println(currentEvent);
      currentEvent.setSeason();
      String season = (currentEvent.getPreviousSeason(false));
      int i = 0;
      while (i < teamList.size()) {
        ((Team) teamList.get(i)).setSKU(skuOriginal);
        ((Team) teamList.get(i)).setPWP();
        ((Team) teamList.get(i)).setPAP();
        ((Team) teamList.get(i)).setPSP();
        ((Team) teamList.get(i)).setAwards(season);
        ((Team) teamList.get(i)).setSkills();
        ((Team) teamList.get(i)).setPccvm(false);
        ((Team) teamList.get(i)).setPccvm(true);
        ((Team) teamList.get(i)).setfinalScoreNR();
        //System.out.println(((Team) teamList.get(i)).getTeam() + ": " + ((Team) teamList.get(i)).getFinalScore());
        i++;
      }
      Collections.sort(teamList, new Output());
      Collections.reverse(teamList);
      i = 0;
      while (i < teamList.size()) {
        resultList.add(new Result(((Team) teamList.get(i)).getTeam(), ((Team) teamList.get(i)).getFinalScore()));
        i++;
      }
      String finalJson = new Gson().toJson(resultList);
      System.out.println(finalJson);
    }
  }
}
