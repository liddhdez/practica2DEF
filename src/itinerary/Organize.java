
package itinerary;

import java.util.*;

import dictionary.Entry;
import dictionary.HashTableDictionarySC;
import maps.HashTableMapLP;
import material.Pair;

/**
 *
 * @author mayte
 */
public class Organize {
    
    //Nos dan una lista de pares <Origen, Destino> con todos los billetes
    HashTableMapLP<String, String> billetes;
    List<Pair<String,String>> posiblesInicios;

    public Organize (List<Pair<String,String>> lista){
        billetes = new HashTableMapLP<>();
        posiblesInicios = lista;
        for(Pair<String,String> billete : lista){
            //System.out.println(billete.getFirst() + billete.getSecond());
            billetes.put(billete.getFirst(), billete.getSecond());
        }
    }

    /**
     * Returns the itinerary to travel or thrown an exception
     * @return 
     */
    public List<String> itineratio(){
        String inicio = buscaInicio();
        System.out.println(inicio);
        List<String> it = new LinkedList<>();
        it.add(inicio);
        Deque<String> deque = new ArrayDeque<>();
        deque.add(inicio);
        while(!(deque.isEmpty())){
            String aux = deque.poll();
            if(billetes.get(aux) != null) {
                String next = billetes.get(aux);
                it.add(next);
                deque.add(next);
            }
        }
        return it;
    }

    private String buscaInicio(){
        String inicio = "";
        List<String> valores = new LinkedList<>();
        Iterator<String> it =  billetes.values().iterator();
        while(it.hasNext()){
            String next = it.next();
            valores.add(next);
        }

        for(Pair<String,String> pair : posiblesInicios){
            if(!(valores.contains(pair.getFirst()))){
                return pair.getFirst();
            }
        }
        throw new RuntimeException("No se puede pasar por un mismo sitio 2 veces");
    }



}
