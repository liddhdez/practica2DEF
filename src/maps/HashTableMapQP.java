package maps;
/**
 * @param <K> The hey
 * @param <V> The stored value
     */
public class HashTableMapQP<K, V> extends AbstractHashTableMap<K, V> {

    public HashTableMapQP(int size) {
        super(size);
    }

    public HashTableMapQP() {
        super();
    }

    public HashTableMapQP(int p, int cap) {
        super(p,cap);
    }

    @Override
    protected int offset(K key, int i) {
        return 7*i + 13*i*i;
    }


}
