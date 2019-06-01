import java.util.Comparator;

public class Output implements Comparator<Team> {
    public int compare(Team a, Team b) {
        Integer dateComparison = Integer.compare(a.getFinalScore(), b.getFinalScore());
        return dateComparison == 0 ? Integer.compare(a.getFinalScore(), b.getFinalScore()) : dateComparison;
    }
}
