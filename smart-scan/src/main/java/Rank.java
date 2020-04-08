import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.*;


public class Rank {

  public static String getApiURL() {
    return "https://api.vexdb.io/v1/";
  }
  public static void main(String[] urlParams) throws Exception {
    String skuOriginal = urlParams[0];
    String regexCheck = "^RE-VRC-(17|18|19|)-(\\d\\d\\d\\d)";
    Pattern r = Pattern.compile(regexCheck);
    Matcher m = r.matcher(skuOriginal);
    List<Team> teamList = new ArrayList<>();
    List<Result> resultList = new ArrayList<>();
    if (skuOriginal.equals(null) || !m.find()) {
      System.out.println("Not a valid SKU");
      System.exit(0);
    }
    try {
      URL url = new URL(Rank.getApiURL() + "get_rankings?sku=" + skuOriginal);
      InputStreamReader reader = new InputStreamReader(url.openStream());
      JsonParser jsonParser = new JsonParser();
      JsonArray results = jsonParser.parse(reader)
              .getAsJsonObject().getAsJsonArray("result");
      Gson gson = new Gson();
      for (JsonElement result : results) {
        teamList.add(gson.fromJson(result, Team.class));
      }
      Team temp = teamList.get(0);
      Event currentEvent = new Event(temp.getSku());
      currentEvent.setSeason();
      String season = (currentEvent.getSeason());
      for(int i = 0; i<teamList.size(); i++) {
        teamList.get(i).setAwards(season);
        teamList.get(i).setSkills();
        teamList.get(i).setPccvm(false);
        teamList.get(i).setPccvm(true);
        teamList.get(i).setfinalScore();
        i++;
      }
      finalOutputHandler(teamList, resultList);
    }

    catch (Exception IndexOutOfBoundsException) {
      URL url = new URL(Rank.getApiURL() + "get_teams?sku=" + skuOriginal);
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
      for(int i = 0; i<teamList.size(); i++) {
        teamList.get(i).setSKU(skuOriginal);
        teamList.get(i).setPWP();
        teamList.get(i).setPAP();
        teamList.get(i).setPSP();
        teamList.get(i).setAwards(season);
        teamList.get(i).setSkills();
        teamList.get(i).setPccvm(false);
        teamList.get(i).setPccvm(true);
        teamList.get(i).setfinalScoreNR();
      }
      finalOutputHandler(teamList, resultList);
    }
  }
  public static void finalOutputHandler(List<Team> teamList, List<Result> resultList) {
    Collections.sort(teamList, new Output());
    Collections.reverse(teamList);
    for(int i = 0; i<teamList.size(); i++) {
      resultList.add(new Result(teamList.get(i).getTeam(), teamList.get(i).getFinalScore()));
    }
    String finalJson = new Gson().toJson(resultList);
    System.out.println(finalJson);
  }
}
