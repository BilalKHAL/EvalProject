import java.util.*;

public class ishish implements Comparator<Participants> {
	public int compare(Participants p1, Participants p2){
		return p1.gettemps().getSeconds()<p2.gettemps().getSeconds()?-1: 
			(p1.gettemps().getSeconds()==p2.gettemps().getSeconds()?0:1);
			
	}

}

