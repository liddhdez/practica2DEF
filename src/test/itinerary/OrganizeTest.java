
package test.itinerary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import itinerary.Organize;
import material.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mayte
 */
public class OrganizeTest {
    
    public OrganizeTest() {
    }

    /**
     * Test of itineratio method, of class Organize.
     */
    @Test
    public void testItineratio() {
        System.out.println("itineratio");
        ArrayList<Pair<String,String>> l = new ArrayList<>();
        l.add(new Pair<>("New York","Boston"));
        l.add(new Pair<>("Los Angeles","Las Vegas"));
        l.add(new Pair<>("Orlando","New York"));
        l.add(new Pair<>("Las Vegas","Orlando"));
        Organize instance = new Organize(l);
        List<String> expResult = Arrays.asList("Los Angeles","Las Vegas","Orlando","New York","Boston");
        List<String> result = instance.itineratio();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testItinerario2(){
        System.out.println("itinerario2");
        ArrayList<Pair<String,String>> l = new ArrayList<>();
        l.add(new Pair<>("New York","Boston"));
        l.add(new Pair<>("Boston","Las Vegas"));
        l.add(new Pair<>("Orlando","New York"));
        l.add(new Pair<>("Las Vegas","Orlando"));
        try{
            Organize instance = new Organize(l);
            instance.itineratio();
        }catch (RuntimeException e){
            System.out.println(e.toString());
            
        }
        
    }
}
