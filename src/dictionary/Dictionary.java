
package dictionary;

/**
 * An interface for a dictionary that allows the use of the same key for one or more values.
 * 
 * @author mayte
 * @param <K>
 * @param <V>
 */
public interface Dictionary<K,V> extends Iterable<Entry<K,V>> {
    
    /**
     * @return  the number of items in the dictionary. 
     */
    public int size();

    /**
     * @return whether the dictionary is empty.
     */
    public boolean isEmpty();

    /**
     * Adds the value of this
     * entry with the specified value. 
     * @param key
     * @param value
     * @return
     */
    public Entry<K,V> insert(K key, V value) throws IllegalStateException;

    /**
     * Returns the value of the first entry containing the given key. 
     * Returns null if no such entry exists.
     * @param key used in the search
     * @return The first ocurrence of the entry. 
     */
    public Entry<K,V> find(K key) throws IllegalStateException;

    /**
     * Returns an iterable object which allows to visit all the entries containing the given key.
     * @param key used in the search
     * @return An iterator to the first ocurrence of the entry. 
     */
    public Iterable <Entry<K,V>> findAll(K key) throws IllegalStateException;

    
    /**
     * If there exists the entry, it is removed and returned. Else,returns null.
     * @param e
     * @return removed Entry
     */
    public Entry<K,V> remove(Entry<K,V> e) throws IllegalStateException;

   
    /**
     * @return  an iterable object containing all the entries in the map.
     */
    public Iterable<Entry<K, V>> entries();
    
}
