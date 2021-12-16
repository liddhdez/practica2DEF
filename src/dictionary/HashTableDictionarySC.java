
package dictionary;

import java.util.*;

/**
 *
 * @author mayte
 * @param <K>
 * @param <V>
 */
public class HashTableDictionarySC<K,V> implements Dictionary<K,V> {

    private class HashEntry<T, U> implements Entry<T, U> {

        protected T key;
        protected U value;

        public HashEntry(T k, U v) {
            key = k;
            value = v;
        }

        @Override
        public U getValue() {
            return value;
        }

        @Override
        public T getKey() {
            return key;
        }

        public U setValue(U val) {
            U oldValue = value;
            value = val;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {

            if (o.getClass() != this.getClass()) {
                return false;
            }

            HashEntry<T, U> ent;
            try {
                ent = (HashEntry<T, U>) o;
            } catch (ClassCastException ex) {
                return false;
            }
            return (ent.getKey().equals(this.key))
                    && (ent.getValue().equals(this.value));
        }

        /**
         * Entry visualization.
         */
        @Override
        public String toString() {
            return "(" + key + "," + value + ")";
        }
    }

    private class HashDictionaryIterator<T, U> implements Iterator<Entry<T, U>> {

        List<HashEntry<T,U>>[] buck;
        Deque<HashEntry<T,U>> l;

        public HashDictionaryIterator(List<HashEntry<T,U>>[] dic){
            this.buck = dic;
            l = new LinkedList<>();
            for (List<HashEntry<T, U>> hashEntries : this.buck) {
                if (!hashEntries.isEmpty()) {
                    l.addAll(hashEntries);
                }
            }
        }

        @Override
        public boolean hasNext() {
            return(!l.isEmpty());
        }

        @Override
        public Entry<T, U> next() {
            return l.remove();
        }

    }

    protected List<HashEntry<K,V>>[] bucket;
    protected int n;
    protected int prime, capacity;
    protected long scale, shift;

    public HashTableDictionarySC(){
        this.capacity = 20;
        this.n = 0;
        this.prime = 109345121;
        Random r = new Random();
        this.scale = r.nextInt(this.prime-1)+1;
        this.shift = r.nextInt(this.prime);
        this.bucket = (List<HashEntry<K,V>>[]) new List[capacity];
        for(int i = 0; i<this.capacity; i++){
            this.bucket[i] = new LinkedList<>();
        }
    }

    /**
     * Hash function applying MAD method to default hash code.
     *
     * @param key Key
     * @return
     */
    private int hashValue(K key) { //h(y) = ((ay+b) mod p)mod N
        int khc = key.hashCode();
//        System.out.println(scale);
//        System.out.println(shift);
//        System.out.println(prime);
//        System.out.println(capacity);
//        return (int) ((Math.abs(key.hashCode() * this.scale + this.shift) % this.prime) % this.capacity);
        return (int) ((((key.hashCode()*this.scale)+this.shift) % this.prime) % this.capacity);
    }

    @Override
    public int size() {
        return this.n;
    }

    @Override
    public boolean isEmpty() {
        return this.n == 0;
    }

    @Override
    public Entry<K, V> insert(K key, V value) throws IllegalStateException {
        checkKey(key);
        HashEntry<K,V> node = new HashEntry<>(key, value);
        if(this.size() > this.capacity*0.75){
            rehash();
        }
        int index = hashValue(key);
        this.bucket[index].add(node);
        this.n++;
        return node;
    }

    @Override
    public Entry<K, V> find(K key) throws IllegalStateException {
        checkKey(key);
        int index = hashValue(key);
        for(HashEntry<K,V> p : bucket[index]){
            if(p.getKey().equals(key)){
                return p;
            }
        }
        return null;
    }

    @Override
    public Iterable<Entry<K, V>> findAll(K key) throws IllegalStateException {
        checkKey(key);
        int index = hashValue(key);
        List<Entry<K,V>> l = new LinkedList<>();
        for(HashEntry<K,V> p : bucket[index]){
            if(p.getKey().equals(key)){
                l.add(p);
            }
        }
        return l;
    }

    @Override
    public Entry<K, V> remove(Entry<K, V> e) throws IllegalStateException {
        checkKey(e.getKey());
        int index = hashValue(e.getKey());
        HashEntry<K,V> hEntry = (HashEntry<K,V>)e;
        if(this.bucket[index].contains(hEntry)) {
            this.bucket[index].remove(hEntry);
            this.n--;
            return hEntry;
        }
        return hEntry;
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        List<Entry<K,V>> l = new LinkedList<>();
        Iterator<Entry<K, V>> it = iterator();
        while(it.hasNext()){
            l.add(it.next());
        }
        return l;
//        return new HashDictionaryIterator<K,V>(bucket);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashDictionaryIterator<K,V>(bucket);
    }

    /**
     * Doubles the size of the hash table and rehashes all the entries.
     */
    private void rehash() {
        this.capacity = this.capacity * 2;
        List<HashEntry<K, V>>[] old = this.bucket;
        this.bucket = (List<HashEntry<K, V>>[]) new List[this.capacity];
        for (int i = 0; i < this.capacity; i++)
            this.bucket[i] = new LinkedList<>();
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
        for(List<HashEntry<K,V>> l:old)
            for (HashEntry<K, V> e : l) {
                int index = hashValue(e.getKey());
                this.bucket[index].add(e);
            }
    }

    protected void checkKey(K k) {
        if (k == null) {
            throw new IllegalStateException("Invalid key: null.");
        }
    }
}
