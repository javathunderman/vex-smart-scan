import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.*;


public class Rank {

  public static void main(String[] urlParams) throws Exception {
    String urlParam = urlParams[0];
    String regexCheck = "^RE-VRC-(17|18|19|20)-(\\d\\d\\d\\d)";
    Pattern XSSCheck = Pattern.compile(regexCheck);
    Matcher XSSMatcher = XSSCheck.matcher(urlParam);
    if (urlParam.equals(null)) {
      System.out.println("u w0t m8"); //lol
      System.exit(0);
    }
    if (!XSSMatcher.find()) { //if regex check fails server check and jar check, terminate
      System.out.println("Not a valid SKU");
      System.exit(0);
    }

    try {
      URL getRankings = new URL("https://api.vexdb.io/v1/get_rankings?sku=" + urlParam);
      InputStreamReader rankingsReader = new InputStreamReader(getRankings.openStream());
      List teamList = new ArrayList<Team>();
      List resultList = new ArrayList<Result>();
      JsonParser jsonParser = new JsonParser();
      JsonArray results = jsonParser.parse(rankingsReader)
              .getAsJsonObject().getAsJsonArray("result");
      Gson gson = new Gson();
      for (JsonElement result : results) {
        teamList.add(gson.fromJson(result, Team.class));
      }
      Team temp = (Team) teamList.get(0);
      Event currentEvent = new Event(temp.getSku()); //fetch SKU from vexDB API response to avoid reusing URL parameter
      currentEvent.setSeason();
      String season = (currentEvent.getSeason());
      int i = 0;
      while (i < teamList.size()) {
        ((Team) teamList.get(i)).setOldSKU();
        ((Team) teamList.get(i)).setAwards(season);
        ((Team) teamList.get(i)).setSkills();
        ((Team) teamList.get(i)).setPccvm();
        ((Team) teamList.get(i)).setfinalScore();
        i++;
      }
      Collections.sort(teamList, new Output());
      Collections.reverse(teamList); //order the teamList by lowest-highest score, then reverse
      i = 0;
      while (i < teamList.size()) {
        resultList.add(new Result(((Team) teamList.get(i)).getTeam(), ((Team) teamList.get(i)).getFinalScore()));
        i++;
      }
      String finalJson = new Gson().toJson(resultList);
      System.out.println(finalJson);
    }
    catch (Exception IndexOutOfBoundsException) {
      URL getTeams = new URL("https://api.vexdb.io/v1/get_teams?sku=" + urlParam);
      InputStreamReader teamsReader = new InputStreamReader(getTeams.openStream());
      List teamList = new ArrayList<Team>();
      List resultList = new ArrayList<Result>();
      JsonParser jsonParser = new JsonParser();
      JsonArray results = jsonParser.parse(teamsReader)
              .getAsJsonObject().getAsJsonArray("result");
      Gson gson = new Gson();
      for (JsonElement result : results) {
        teamList.add(gson.fromJson(result, Team.class));
      }
      Event currentEvent = new Event(urlParam);
      currentEvent.setSeason();
      String season = (currentEvent.getPreviousSeason(false));
      int i = 0;
      while (i < teamList.size()) {
        ((Team) teamList.get(i)).setSKU(urlParam);
        ((Team) teamList.get(i)).setPWP();
        ((Team) teamList.get(i)).setPAP();
        ((Team) teamList.get(i)).setPSP();
        ((Team) teamList.get(i)).setAwards(season);
        ((Team) teamList.get(i)).setSkills();
        ((Team) teamList.get(i)).setPccvm();
        ((Team) teamList.get(i)).setfinalScoreNR();
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
