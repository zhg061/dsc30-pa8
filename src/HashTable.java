/*
 * NAME: Zhaoyi Guo
 * PID: A15180402
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * class that implements hashTable
 */
public class HashTable implements IHashTable {
	//You will need a HashTable of LinkedLists.
	
	private int nelems;  //Number of element stored in the hash table
	private int expand;  //Number of times that the table has been expanded
	private int collision;  //Number of collisions since last expansion
	private String statsFileName;     //FilePath for the file to write statistics upon every rehash
	private boolean printStats = false;   //Boolean to decide whether to write statistics to file or not after rehashing
	
	//You are allowed to add more :)
	LinkedList<String>[] table;
	double loadFactor;
	public static final int NUMBITS = 8;
	final double loadStandard = (double) 2 / (double) 3;
	final int expandFactor = 2;
	private int longestChain;
	DecimalFormat df = new DecimalFormat("#.##");
	static int functionFactor = 27;
	static int WORD_WIDTH = 4 * NUMBITS;
	static int WEISS_HASH_SHIFT = 5;




	/**
	 * The constructor that creates a hash table of a given size.
	 * If a user uses this constructor, printStats should be set
	 * to false and do not use printTable() to
	 * print the stats before each resizing.
 	 * @param size
	 */
	public HashTable(int size) {
		
		//Initialize the array of linked list with the size size
		table = new LinkedList[size];

	}

	/**
	 * The constructor that creates a hash table of a given size.
	 * If a user uses this constructor, printStats should be set
	 * to true and you should use printStatistics()
	 * to print the stats to given file before each resizing.
	 * @param size
	 * @param fileName
	 */
	public HashTable(int size, String fileName) {
		
		// Set printStats to true and statsFileName to fileName
		printStats = true;
		statsFileName = fileName;
		table = new LinkedList[size];
	}

	/**
	 * Inserts a value into the hash table.
	 * Return true if value is successfully inserted,
	 * false if it can’t be inserted (a value can’t be inserted
	 * if it already exists in the hash table).
	 * Throw a NullPointerException if a null value is passed.
	 * @param value value to insert
	 * @return
	 */
	public boolean insert(String value) {
		
		//get the loading factor, it it exceeds 2/3, we need to rehash
		loadFactor = (double) nelems / (double) table.length;
		if (loadFactor > loadStandard)
			rehash();
		// throw exception if the value is null
		if (value == null)
			throw new NullPointerException();
		// get the index by using the hash function
		int curIndex = hashVal(value);
		//if there is no linked list at that index, we need to create one
		if (table[curIndex] == null) {
			table[curIndex] = new LinkedList<>();
		}
		// if there are more than one element int hat linked list we
		// need to increment nelems
		if (table[curIndex].size() > 0) {
			collision++;
		}
		nelems++;
		// add value to the linked list
		return table[curIndex].add(value);
	}

	/**
	 * Use the hash table to find value and delete
	 * it from the hash table.
	 * Return true if the value is successfully deleted,
	 * false if it can’t be deleted
	 * (a value can’t be deleted if it does not exist in the hash table).
	 * Throw a NullPointerException if a null value is passed.
	 * @param value value to delete
	 * @return
	 */
	public boolean delete(String value) {
		
		//if the value is null throw exception
		if (value == null)
			throw new NullPointerException();
		// get the index of that value
		int curIndex = hashVal(value);
		//if the value is found, we need to remove it
		if (lookup(value)) {
			nelems--;
			table[curIndex].remove(value);
			return true;
		}
		// if not found return false
		return false;
	}
	/**
	 * Use the hash table to find value in the hash table.
	 * Return true if the hash table contains the value, false otherwise.
	 * Throw a NullPointerException if a null value is passed.
	 * @param value value to delete
	 * @return
	 */
	public boolean lookup(String value) {
		
		//if the value is null, throw exception
		if (value == null)
			throw new NullPointerException();
		int curIndex = hashVal(value);
		// if table at that index is null, or table does not contain than value
		// return false
		if (table[curIndex] == null || !table[curIndex].contains(value)) {
			return false;
		}
		return true;
	}

	/**
	 * Print out the hash table
	 */
	public void printTable() {

		//iterate through the table
		for (int i = 0; i < table.length; i++) {
			System.out.print(i + " :");
			if (table[i] != null) {
				// iterate through the linked list
				for (int j = 0; j < table[i].size(); j++) {
					System.out.print(" " + table[i].get(j));
				}
			}
			System.out.println();
		}
	}

	/**
	 * Return the number of elements currently stored in the hash table
 	 * @return nelems
	 */
	public int getSize() {

		//get the total number of elements
		return nelems;
	}

	/**
	 * Return the hash value of the given string.
	 * @param str
	 * @return
	 */
	private int hashVal(String str) {

		//hash function from online sources
		//https://www.cpp.edu/~ftang/courses/CS240/lectures/hashing.htm
		char[] array = str.toCharArray();
		int hashValue = 0;
		for (int i = 0; i < array.length; i++) {
			hashValue = (hashValue * functionFactor) + (int) (array[i]);
			hashValue %= table.length;
		}
		return hashValue;
	}

	/**
	 * Double the size of hash table and rehash all the entries.
	 */
	private void rehash() {

		//if the loadfactor exceeds 2/3 we need to rehash

		if (printStats)
			printStatistics();
		// everything changes back to empty
		LinkedList<String>[] table1 = table;
		table = new LinkedList[table.length * expandFactor];
		collision = 0;
		nelems = 0;
		// iterate through table1,
		for (int i = 0; i < table1.length; i++) {
			if (table1[i] == null)
				continue;
			// insert value again to get new index through hash function
			for (int j = 0; j < table1[i].size(); j++) {
				insert(table1[i].get(j));
			}
		}
		expand++;

	}

	/**
	 * Print statistics of your hash table
	 */
	private void printStatistics() {
		//* PrintWriter must be put in try catch block to catch
		//potential exception */
		String loadFactor1 = df.format(loadFactor);
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null && table[i].size() > longestChain)
				longestChain = table[i].size();
		}
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(new
					File(statsFileName), true));
			pw.println(expand + " resizes, load factor "
					+ loadFactor1 + ", " + collision + " collisions, " + longestChain + " longest chain");
			pw.close();
		} catch (IOException e) { // If the given file doesn’t exist
			System.out.println("File not found!");


		}

	}
}
