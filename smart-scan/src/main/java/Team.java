import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

public class Team {
  @SerializedName(value="team", alternate="number") private String team;
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
  private int finalScore;

  public Team(String team, String sku, double ccwm, int wp, int ap, int sp, double trsp) {
    this.team = team;
    this.sku = sku;
    this.ccwm = ccwm;
    this.wp = wp;
    this.ap = ap;
    this.sp = sp;
    this.trsp = trsp;
    this.pccvm = 0.0;
  }
  public Team(String team) {
    this.team = team;
    this.sku = "";
    this.ccwm = 0.0;
    this.wp = 0;
    this.ap = 0;
    this.sp = 0;
    this.trsp = 0;
    this.pccvm = 0.0;
  }
  public void setSKU(String skuOriginal) {
    this.sku = skuOriginal;
  }
  public void setfinalScore() {
    double tmpfinalScore = (3*ccwm) + (1.5*awards) + 1.5*(pccvm + twopCCVM) + (3*skills) + (0.3*wp) + (0.2*(ap+sp));
    this.finalScore = ((int)tmpfinalScore);
  }
  public void setfinalScoreNR() {
    double tmpfinalScore = (1.5*awards) + 3*(pccvm + twopCCVM) + (3*skills) + (0.3*wp) + (0.2*(ap+sp));
    this.finalScore = ((int)tmpfinalScore);
  }
  public int getFinalScore() {
    return finalScore;
  }
  public String toString() {
    String output = "Team: " + team +"\nccwm: " + ccwm + "\nwp: " + wp + "\nap: " + ap + "\nsp: " + sp + "\ntrsp: " + trsp + "\nawards: " + awards + "\nSkills: " + skills +"\npccvm: " + pccvm + "\ntwoPCCVM: " + twopCCVM;
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
  public void setPccvm(boolean twoyears) throws IOException {
    
    try {
      if(twoyears) {
        this.twopCCVM = (PreviousSeasons.setPCCWM(team, sku, twoyears));
      }
      else {
        this.pccvm = (PreviousSeasons.setPCCWM(team, sku, twoyears));
      }

    }
    catch (Exception e) {
      if(twoyears) {
        this.twopCCVM = 0.0;
        
      }
      else {
        this.pccvm = 0.0;
        
      }

    }

  }
  private static Date getDateNearest(List<Date> dates, Date targetDate){
    return new TreeSet<Date>(dates).lower(targetDate);
  }
  public void setPWP() {
    try {
      this.wp = (PreviousSeasons.setPWASP(team, sku, "wp"));
    }
    catch (Exception e) {
      this.wp = 0;
    }
  }
  public void setPAP() {
    try {
      this.ap = (PreviousSeasons.setPWASP(team, sku, "ap"));
    }
    catch (Exception e) {
      this.ap = 0;
    }
  }
  public void setPSP() {
    try {
      this.sp = (PreviousSeasons.setPWASP(team, sku, "sp"));
    }
    catch (Exception e) {
      this.sp = 0;
    }
  }
}
