import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Team {
  private String team;
  private double ccwm;
  private double pccvm;
  private double twopCCVM;
  private int wp;
  private int ap;
  private int sp;
  private double trsp;
  private int awards = 0;
  private int skills;
  private String sku;
  private double finalScore;

  public Team(String team, String sku, double ccwm, int wp, int ap, int sp, double trsp) {
    this.team = team;
    this.sku = sku;
    this.ccwm = ccwm;
    this.wp = wp;
    this.ap = ap;
    this.sp = sp;
    this.trsp = trsp;
  }
  public double finalScore() {
    return((3*ccwm) + (1.5*awards) + 1.5*(pccvm + twopCCVM) + (3*skills) + (0.3*wp) + (0.2*(ap+sp)));
  }
  public String toString() {
    String output = "Team: " + team +"\nccwm: " + ccwm + "\nwp: " + wp + "\nap: " + ap + "\nsp: " + sp + "\ntrsp: " + trsp + "\nawards: " + awards + "\nSkills: " + skills +"\n";
    return(output);
  }
  public String getTeam() {
    return(team);
  }
  public double getccwm() {
    return(ccwm);
  }
  public double getpccvm() {
    return pccvm;
  }
  public double get2pccvm() {
    return twopCCVM;
  }
  public int getWP() {
    return wp;
  }
  public int getAP() {
    return ap;
  }
  public int getSP() {
    return sp;
  }
  public double getTRSP() {
    return trsp;
  }
  public int getAwardsTotal() {
    return awards;
  }
  public int getSkills() {
    return skills;
  }
  public String getSku() {
    return sku;
  }
  public void setAwards(String season) throws IOException {
    URL url = new URL("http://api.vexdb.io/v1/get_awards?team=" + team + "&season=" + season);
    ArrayList<String> awardList = new ArrayList<String>();
    InputStreamReader reader = new InputStreamReader(url.openStream());
    JsonParser jsonParser = new JsonParser();
    JsonArray results = jsonParser.parse(reader)
            .getAsJsonObject().getAsJsonArray("result");
    for(JsonElement result: results) {
      JsonObject temp = result.getAsJsonObject();
      String stuff = String.valueOf(temp.get("name"));
      awardList.add(stuff);
    }
    for(String award: awardList) {
      if(award.contains("Excellence"))
        awards+=8;
      else if(award.contains("Champions"))
        awards+=10;
      else if(award.contains("Semifinalists"))
        awards+=7;
      else if(award.contains("Judges"))
        awards+=3;
      else
        awards+=6;
    }
  }
  public int getAwards() {
    return awards;
  }
  public void setSkills() throws IOException {
    URL url = new URL("http://api.vexdb.io/v1/get_skills?type=2&team=" + team + "&sku=" + sku);
    InputStreamReader reader = new InputStreamReader(url.openStream());
    JsonParser jsonParser = new JsonParser();
    JsonArray results = (JsonArray) jsonParser.parse(reader).getAsJsonObject().get("result"); //idk what the hell this is
    for(JsonElement result: results) {
      JsonObject temp = result.getAsJsonObject();
      String stuff = String.valueOf(temp.get("score"));
      skills = Integer.valueOf(stuff);

    }
  }
}
