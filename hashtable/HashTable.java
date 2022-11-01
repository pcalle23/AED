package aed.hashtable;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import es.upm.aedlib.Entry;
import es.upm.aedlib.EntryImpl;
import es.upm.aedlib.map.Map;
import es.upm.aedlib.InvalidKeyException;
/**
 * A hash table implementing using open addressing to handle key collisions.
 */
public class HashTable<K, V> implements Map<K, V> {//0
	Entry<K, V>[] buckets;
	int size;
	public HashTable(int initialSize) {
		this.buckets = createArray(initialSize);
		this.size = initialSize;
	}
	/**
	 * Add here the method necessary to implement the Map api, and any auxilliary
	 * methods you deem convient.
	 */
	// Examples of auxilliary methods: IT IS NOT REQUIRED TO IMPLEMENT THEM
	// METODOS AUXILIARES
	@SuppressWarnings("unchecked")
	private Entry<K, V>[] createArray(int size) {//0
		Entry<K, V>[] buckets = (Entry<K, V>[]) new Entry[size];
		return buckets;
	}
	private int index(Object obj) {
		int code = obj.hashCode();
		if(code==0) return 0;
		return Math.abs(code%size);		
		
	}
	private int search(Object obj) {
		int i = index(obj);
		int j = i;

		boolean encontrado = false;
		for (int t=0;t<size&&!encontrado;t++) {
			if (buckets[i] == null || (buckets[i] != null &&buckets[i].getKey().equals(obj))) {
				return i;
			}
			i++;
			if (i >= buckets.length) {
				i = 0;
			}
			else if (j == i) {
				encontrado = true;
			}
		}
		return -1;
	}
	private void rehash() {
		Entry<K, V>[] buckets2 = (Entry<K, V>[]) new Entry[buckets.length * 2];
		size = buckets.length * 2;
		int i=0;
		while(i<buckets.length) {
			if (buckets[i] != null) {
				int j = index(buckets[i].getKey());
				while (buckets2[j] != null) {
					j++;
					if (j >= size) {
						j = 0;
					}
				}
				buckets2[j] = buckets[i];
			}
			i++;
		}
		buckets = buckets2;
	}
	// METODOS A REALIZAR
	@Override
	public Iterator<Entry<K, V>> iterator() {
		return null;
	}
	@Override
	public boolean containsKey(Object arg0) throws InvalidKeyException {
		int i=0;
		if(arg0 == null)
			throw new InvalidKeyException();
		while (i < buckets.length) {
			if (buckets[i] != null && arg0.equals(buckets[i].getKey())) {
				return true;
			}
			i++;
		}
		return false;
	}
	@Override
	public Iterable<Entry<K, V>> entries() {
		List<Entry<K, V>> list2 = new ArrayList<>(this.size());
		for(Entry<K, V> entry : buckets) {
			int size= list2.size();
			if(entry != null)
				list2.add(size,entry);
		}
		Iterable<Entry<K, V>> keysIterable = (Iterable<Entry<K, V>>) list2;
		return keysIterable;
	}
	@Override
	public V get(K arg0) throws InvalidKeyException {
		for (Entry<K, V> entry : buckets) {
			if(entry!= null) {
				if(entry.getKey().equals(arg0))
					return entry.getValue();
			}
		}
		return null;
	}
	@Override
	public boolean isEmpty() {//0
		for (int i = 0; i < buckets.length; i++) {
			if (buckets[i] != null) {
				return false;
			}
		}
		return true;
	}
	@Override
	public Iterable<K> keys() {//*
		List<Integer> list1 = new ArrayList<>(this.size());
		for(Entry<K, V> entry : buckets) {
			if(entry != null)
				list1.add(Integer.parseInt(entry.getKey().toString()));
		}
		Iterable<K> keysIterable = (Iterable<K>) list1;
		return keysIterable;
	}
	@Override
	public V put(K arg0, V arg1) throws InvalidKeyException {
		V salida = null;
		int i = search(arg0);
		if (i == -1) {
			rehash();
			i = search(arg0);
		}
		if (buckets[i] == null) {
			buckets[i] = new EntryImpl<K, V>(arg0, arg1);
		} else {
			if (this.containsKey(arg0)) {
				if (buckets[i] != null) {
					salida = buckets[i].getValue();
					buckets[i] = new EntryImpl<K, V>(arg0, arg1);
				}
			}
		}
		return salida;
	}
	@Override
	public V remove(K arg0) throws InvalidKeyException {
		if (this.containsKey(arg0)) {
			boolean encontrado = false;
			int i =0;
			while (i < buckets.length && !encontrado) {
				if (buckets[i] != null && buckets[i].getKey().equals(arg0)) {
					V salida = buckets[i].getValue();
					buckets[i] = null;
					return salida;
				}
				i++;
			}
		}
		return null;
	}
	@Override
	public int size() {
		int j = 0;
		int i=0;
		if (buckets != null) {
			while (i < buckets.length) {
				if (buckets[i] != null) {
					j++;
				}
				i++;
			}
		}
		return j;
	}
} 
