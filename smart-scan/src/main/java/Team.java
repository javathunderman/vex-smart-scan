import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Team {
  private String team;
  private double ccwm;
  private double pccvm;
  private double twopCCVM;
  private int wp;
  private int ap;
  private int sp;
  private double trsp;
  private int awards;
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
    String output = "Team: " + team +"\nccwm: " + ccwm + "\nwp: " + wp + "\nap" + ap + "\nsp: " + sp + "\ntrsp: " + trsp;
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
    System.out.println(url);
    InputStreamReader reader = new InputStreamReader(url.openStream());
    JsonParser jsonParser = new JsonParser();
    JsonArray results = jsonParser.parse(reader)
            .getAsJsonObject().getAsJsonArray("result");
    for (JsonElement result : results) {
      System.out.println(result);
    }
  }
}
