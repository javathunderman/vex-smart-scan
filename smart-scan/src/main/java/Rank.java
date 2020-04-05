import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.*;


public class Rank {

  public static void main(String[] urlParams) throws Exception {
    String skuOriginal = urlParams[0];
    String regexCheck = "^RE-VRC-(17|18|19|)-(\\d\\d\\d\\d)";
    Pattern r = Pattern.compile(regexCheck);
    Matcher m = r.matcher(skuOriginal);
    ArrayList<Team> teamList = new ArrayList<Team>();
    ArrayList<Result> resultList = new ArrayList<Result>();
    if (skuOriginal.equals(null) || !m.find()) {
      System.out.println("Not a valid SKU");
      System.exit(0);
    }
    int i = 0;
    try {
      URL url = new URL("http://api.vexdb.io/v1/get_rankings?sku=" + skuOriginal);
      InputStreamReader reader = new InputStreamReader(url.openStream());
      JsonParser jsonParser = new JsonParser();
      JsonArray results = jsonParser.parse(reader)
              .getAsJsonObject().getAsJsonArray("result");
      Gson gson = new Gson();
      for (JsonElement result : results) {
        teamList.add(gson.fromJson(result, Team.class));
      }
      Team temp = (Team) teamList.get(0);
      Event currentEvent = new Event(temp.getSku());
      currentEvent.setSeason();
      String season = (currentEvent.getSeason());
      while (i < teamList.size()) {
        ((Team) teamList.get(i)).setAwards(season);
        ((Team) teamList.get(i)).setSkills();
        ((Team) teamList.get(i)).setPccvm(false);
        ((Team) teamList.get(i)).setPccvm(true);
        ((Team) teamList.get(i)).setfinalScore();
        i++;
      }
      finalOutputHandler(teamList, resultList);
    }

    catch (Exception IndexOutOfBoundsException) {
      URL url = new URL("http://api.vexdb.io/v1/get_teams?sku=" + skuOriginal);
      InputStreamReader reader = new InputStreamReader(url.openStream());
      JsonParser jsonParser = new JsonParser();
      JsonArray results = jsonParser.parse(reader)
              .getAsJsonObject().getAsJsonArray("result");
      Gson gson = new Gson();
      for (JsonElement result : results) {
        teamList.add(gson.fromJson(result, Team.class));
      }
      Event currentEvent = new Event(skuOriginal);
      currentEvent.setSeason();
      String season = (currentEvent.getPreviousSeason(false));
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
        i++;
      }
      finalOutputHandler(teamList, resultList);
    }
  }
  public static void finalOutputHandler(ArrayList<Team> teamList, ArrayList<Result> resultList) {
    Collections.sort(teamList, new Output());
    Collections.reverse(teamList);
    int i = 0;
    while (i < teamList.size()) {
      resultList.add(new Result(((Team) teamList.get(i)).getTeam(), ((Team) teamList.get(i)).getFinalScore()));
      i++;
    }
    String finalJson = new Gson().toJson(resultList);
    System.out.println(finalJson);
  }
}
