import java.time.Instant;
import java.time.Duration;

public class TestPerf{

    public static void main(String[] args){
	Resultats resultats = new Resultats();
	resultats.lecture("results.csv");
	Instant start = Instant.now();
	resultats.makeTeam();
	Instant end = Instant.now();
	Duration d = Duration.between(start,end);
	System.out.println(d);
    }
    
}
