/*
 * Hieu Trung Nguyen
 */

import java.lang.reflect.Array;
import java.util.*;

/*
 * Hash Table keeping track of keys and the values associated
 * with them.
 */
public class MyHashTable<K, V> {
	private Entry[] table;
	private Set<String> keySet;
	private double totalProbe;
	private int totalEntries;
	private int maxProbe;
	private int maxSize;
	
	/*
	 * Construct a new MyHashTable given a capacity
	 */
	@SuppressWarnings("unchecked")
	public MyHashTable(int capacity) {
		// thanks coderevisted.com for a tutorial of creating generic arrays
		table = (Entry[])Array.newInstance(Entry.class, capacity);
		maxSize = capacity;
		keySet = new HashSet<String>();
	}
	
	/*
	 * Put the given key and value into the hash table
	 */
	public void put(K searchKey, V newValue) {
		Entry newEntry = new Entry(searchKey, newValue);
		int hashValue = hash(searchKey);
		while (table[hashValue] != null && !table[hashValue].getKey().equals(searchKey)) {
			hashValue = (hashValue + 1) % maxSize;
			int currentProbe = newEntry.getProbeCount() + 1;
			newEntry.setProbeCount(currentProbe);
			totalProbe++;
			if (currentProbe >= maxProbe) {
				maxProbe = currentProbe;
			}
		}
		table[hashValue] = newEntry;
		keySet.add(searchKey.toString());
		totalEntries++;
	}
	
	/*
	 * Given a key, get and return the value associated with that key
	 */
	public V get(K searchKey) {
		int hashValue = hash(searchKey);
		while (table[hashValue] != null && !table[hashValue].getKey().equals(searchKey)) {
			hashValue = (hashValue + 1) % maxSize;
		}
		if (table[hashValue] != null) {
			return (V) table[hashValue].getValue();
		} else {
			return null;
		}
	}
	
	/*
	 * Returns whether the table contains the given key.
	 */
	public boolean containsKey(K searchKey) {
		int hashValue = hash(searchKey);
		while (table[hashValue] != null) {
			if (table[hashValue].getKey().equals(searchKey)) {
				return true;
			}
			hashValue = (hashValue + 1) % maxSize;
		}
		return false;
	}
	
	/*
	 * Display the HashTable statistics.
	 */
	public void stats() {
		System.out.println("Hash Table Stats");
		System.out.println("================");
		System.out.println("Number of Entries: " + totalEntries);
		System.out.println("Number of Buckets: " + maxSize);
		int[] histogram = new int[maxProbe + 1];
		for (int i = 0; i < maxSize; i++) {
			if (table[i] != null) {
				histogram[table[i].probeCount]++;
			}
		}
		System.out.println("Histogram of Probes: " + Arrays.toString(histogram));
		System.out.println("Filled Percentage: " + String.format("%.4f", 1.0 * totalEntries / maxSize * 100.0) + "%");
		System.out.println("Max Linear Probe: " + maxProbe);
		System.out.println("Average Linear Probe: " + String.format("%.4f", 1.0 * totalProbe / totalEntries) + "\n\n");
	}
	
	/*
	 * Return a string representation of the hash table.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < maxSize; i++) {
			Entry currentEntry = table[i];
			if (currentEntry != null) {
				sb.append("(");
				sb.append(currentEntry.getKey().toString());
				sb.append(", ");
				sb.append(currentEntry.getValue().toString());
				sb.append("), ");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/*
	 * Not part of the required interface, but I found this to be useful
	 * for this assignment. 
	 */
	public Set<String> keySet() {
		return keySet;
	}
	
	/*
	 * Given a key, return a hash integer index for the table.
	 */
	private int hash(K searchKey) {
		String key = searchKey.toString();
		int returnValue = 0;
		int prime = 23;
		for (int i = 0; i < key.length(); i++) {
			int value = ((int) key.charAt(i));
			returnValue = returnValue * prime + value;
		}
		returnValue += (key.length() % prime);
		return Math.abs(returnValue % maxSize);
	}
	
	/*
	 * Entry datas for the table
	 */
	private class Entry {
		private K key;
		private V value;
		private int probeCount;
		
		/*
		 * Construct a new entry
		 */
		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
			probeCount = 0;
		}
		
		/*
		 * Return the key
		 */
		public K getKey() {
			return key;
		}
		
		/*
		 * Return the value
		 */
		public V getValue() {
			return value;
		}
		
		/*
		 * Return the number of probes for this entry
		 */
		public int getProbeCount() {
			return probeCount;
		}
		
		/*
		 * Set the number of probes for this entry
		 */
		public void setProbeCount(int count) {
			probeCount = count;
		}
	}
}

// http://coderevisited.com/arrays-of-generic-types/
