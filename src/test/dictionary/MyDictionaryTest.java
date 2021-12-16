
package test.dictionary;

import java.util.ArrayList;
import java.util.Iterator;

import dictionary.Entry;
import dictionary.HashTableDictionarySC;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mayte
 */
public class MyDictionaryTest {
    
    private HashTableDictionarySC<String,String> instance;
    public MyDictionaryTest() {
    }

    
    /**
     * Test of size method, of class MyDictionary.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        instance = new HashTableDictionarySC<>();
        assertEquals(instance.size(),0);
        
        instance.insert("Jose", "912127659");
        Entry<String, String> e = instance.insert("Angel", "912127658");
        instance.insert("Abraham", "912127657");
        instance.insert("Mayte", "912127656");
        instance.insert("Raul", "912127655");
        assertEquals(instance.size(),5);
        
        instance.remove(e);
        assertEquals(instance.size(),4);
    }

    /**
     * Test of isEmpty method, of class MyDictionary.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        instance = new HashTableDictionarySC<>();
        assertTrue(instance.isEmpty());
        Entry<String, String> e = instance.insert("Angel", "912127658");
        assertFalse(instance.isEmpty());
        instance.remove(e);
        assertTrue(instance.isEmpty());
    }

    /**
     * Test of insert and find methods, of class MyDictionary.
     */
    @Test
    public void testInsertAndFind() {
        System.out.println("insert and find");
        instance = new HashTableDictionarySC<>();
        
        try{
            instance.find(null);
            fail("Deberia lanzar: InvalidKeyException");
        }catch (RuntimeException e){
            
        }
        
        try{
            instance.insert(null, "cosa");
            fail("Deberia lanzar: InvalidKeyException");
        }catch (RuntimeException e){
            
        }
        
        

        Entry<String, String> jose = instance.insert("Jose", "912127654");
        Entry<String, String> mayte = instance.insert("Mayte", "912127651");
        Entry<String, String> andres = instance.insert("Andres", "912127624");
        assertEquals(instance.size(), 3);
        assertEquals(instance.find("jose").getValue(), "912127654");
        instance.remove(jose);
        assertEquals(instance.find("jose"), null);
        Entry<String, String> andres2 = instance.insert("Andres", "654321987");
        Iterable<Entry<String, String>> findAll = instance.findAll("Andres");
        for (Entry<String, String> entry : findAll) {
            assertTrue(entry.getKey().equals(andres.getKey()));
            String value = entry.getValue();
            assertTrue((value.equals(andres.getValue()))||(value.equals(andres2.getValue())));
            
        }
    }

    
    /**
     * Test of remove method, of class MyDictionary.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        instance = new HashTableDictionarySC<>();
        Entry<String, String> jose = instance.insert("Jose", "912127654");
        Entry<String, String> mayte = instance.insert("Mayte", "912127651");
        instance.remove(mayte);
        assertEquals(instance.size(),1);
        assertEquals(instance.find("Jose").getValue(), jose.getValue());
        assertEquals(instance.find("Mayte"), null);
        try {
            instance.remove(null);
            fail("Deberia lanzar: InvalidKeyException");

        } catch (RuntimeException e) {
            // OK
        }
        
    }

    /**
     * Test of entries method, of class MyDictionary.
     */
    @Test
    public void testEntries() {
        System.out.println("entries");
        instance = new HashTableDictionarySC<>();
         ArrayList<Entry<String,String>> l = new ArrayList<>();
        Entry<String, String> jose = instance.insert("Jose", "912127654");
        l.add(jose);
        Entry<String, String> mayte = instance.insert("Mayte", "912127651");
        l.add(mayte);
        Entry<String, String> andres = instance.insert("Andres", "912127624");
        l.add(andres);
        Entry<String, String> e = instance.insert("Angel", "912127658");
        l.add(e);
        Iterable<Entry<String,String>> expResult = instance.entries();
        for (Entry<String, String> entry : expResult) {
            assertTrue(l.contains(entry));
        }
        
        
    }

    /**
     * Test of rehash the Dictionary.
     */
    @Test
    public void testRehash() {
        System.out.println("iterator");
        HashTableDictionarySC<Integer,Integer> instance = new HashTableDictionarySC<>();
        final int NUM_ENTRIES = 100_000;
        
        // Testing size
        for (int cont = 0; cont < NUM_ENTRIES; cont++) {
            instance.insert(cont, cont);
            instance.insert(cont, cont+NUM_ENTRIES);
        }
        assertEquals(instance.size(), 2*NUM_ENTRIES);
        
        
    }
    
}
