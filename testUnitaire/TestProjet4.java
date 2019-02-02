
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.FileDescriptor;


public class TestProjet4 {

@Test
public void testClassementGeneral(){
    Resultats resultats = new Resultats();
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    System.setOut(new PrintStream(bout));
    resultats.lecture("results.csv");
    resultats.scratch();
    String res = bout.toString();
    Scanner scan = new Scanner(res);
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    String line = scan.nextLine();
    assertTrue("Should contain Kipyego", line.contains("Kipyego"));
}

    
   @Test
public void testClassementFemme(){
    Resultats resultats = new Resultats();
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    System.setOut(new PrintStream(bout));
    resultats.lecture("results.csv");
    resultats.women();
    String res = bout.toString();
    Scanner scan = new Scanner(res);
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    String line = scan.nextLine();
    assertTrue("Should contain Chepkirui", line.contains("Chepkirui"));
} 

}
