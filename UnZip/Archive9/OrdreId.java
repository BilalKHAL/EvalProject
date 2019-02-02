
import java.util.Comparator;

public class OrdreId implements Comparator<Student> {

	@Override
	public int compare(Student etu1, Student etu2) {
		
		return (etu1.getIdentifiant() < etu2.getIdentifiant()) ? -1 : (etu1.getIdentifiant() == etu2.getIdentifiant()) ? 0 : 1 ; 
		
	}

}
