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

public class HashSort {
    private static HashTable ht;
    protected class HashTable {
        private int nelems;  //Number of element stored in the hash table
        private int expand;  //Number of times that the table has been expanded
        private int collision;  //Number of collisions since last expansion
        private String statsFileName;     //FilePath for the file to write statistics upon every rehash
        private boolean printStats = false;   //Boolean to decide whether to write statistics to file or not after rehashing

        //You are allowed to add more :)
        LinkedList<Integer>[] table;
        double loadFactor;
        public static final int NUMBITS = 8;
        final double loadStandard = (double) 2 / (double) 3;
        final int expandFactor = 2;
        private int longestChain;
        DecimalFormat df = new DecimalFormat("#.##");
        final int functionFactor = 27;
        int max;
        int min;
        int n;
        int range;
        int bucketSize;



        /**
         * The constructor that creates a hash table of a given size.
         * If a user uses this constructor, printStats should be set
         * to false and do not use printTable() to
         * print the stats before each resizing.
         * @param arr, min, max
         */
        public HashTable(int[] arr, int min, int max) {

            //Initialize arr, min, max, n, bucketSize, range
            this.table = new LinkedList[max - min + 1];
            this.n = arr.length;
            this.range = max - min + 1;
            this.bucketSize = (range + n - 1) / n;


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
         * Use the hash table to find value and delete
         * it from the hash table.
         * Return true if the value is successfully deleted,
         * false if it can’t be deleted
         * (a value can’t be deleted if it does not exist in the hash table).
         * Throw a NullPointerException if a null value is passed.
         * @param value value to delete
         * @return
         */
        public boolean delete(int value, int i) {
            //delete the value from the table
            if (value <= 0)
                throw new NullPointerException();
            int curIndex = hashVal(value);
            // if the value is found, deleted it, and return true
            if (lookup(value, i)) {
                nelems--;
                table[curIndex].remove(value);
                return true;
            }

            return false;
        }
        /**
         * Use the hash table to find value in the hash table.
         * Return true if the hash table contains the value, false otherwise.
         * Throw a NullPointerException if a null value is passed.
         * @param value value to delete
         * @return true if value is found, false otherwise
         */
        public boolean lookup(int value, int i) {
            //check if the value is in the table
            if (value <= 0)
                throw new NullPointerException();
            int curIndex = hashVal(value);
            if (table[curIndex] == null || !table[curIndex].contains(value)) {
                return false;
            }
            return true;
        }

        /**
         * Print out the hash table
         */
        public void printTable() {

            //iterate through the loop to print the table
            for (int i = 0; i < table.length; i++) {
                System.out.print(i + " :");
                if (table[i] != null) {
                    for (int j = 0; j < table[i].size(); j++) {
                        System.out.print(" " + table[i].get(j));
                    }
                }
                System.out.println();
            }
        }

        /**
         * Return the number of elements currently stored in the hash table
         * @return
         */
        public int getSize() {

            //get the total num of values in the table
            return nelems;
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
    public void insert(int value) {
        //check whether we need to rehash
        loadFactor = (double) nelems / (double) table.length;
        if (loadFactor > loadStandard)
            rehash();
        // get the index by using the hash function
        int curIndex = hashVal(value);
        // if table at that index is empty, create a new linked list
        if (table[curIndex] == null) {
            table[curIndex] = new LinkedList<>();
        }
        nelems++;
        // if table at that index has other values (collision), we need to put
        // the new index at certain position so it's in ascending order

        int element = 0;
        if (table[curIndex].size() > 0) {
            collision++;
            for (int i = 0; i < table[curIndex].size(); i++) {
                if (table[curIndex].get(i) < value) {
                    element++;
                }
            }
            table[curIndex].add(element, value);
        }
        else {
            table[curIndex].add(value);
        }


        }
        /**
         * Return the hash value of the given string.
         * @param value
         * @return
         */
         private int hashVal(int value) {
             //get the index of the value by hash table
             int result = (value - min) / bucketSize;
             return result;
         }

     /**
      * Double the size of hash table and rehash all the entries.
      */
         private void rehash() {
             //desize the table, and reinsert the value into the table
             if (printStats)
                printStatistics();
             LinkedList<Integer>[] table1 = table;
             table = new LinkedList[table.length * expandFactor];
             collision = 0;
             nelems = 0;
             for (int i = 0; i < table1.length; i++) {
                 if (table1[i] == null)
                     continue;
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
                        + loadFactor1 + ", " + collision + " collisions, "
                        + longestChain + " longest chain");
                pw.close();
            } catch (IOException e) { // If the given file doesn’t exist
                System.out.println("File not found!");


            }

        }
    }

    /**
     * constructor that set the range, and the new hashTable
     * @param arr
     * @param min
     * @param max
     */
    public HashSort(int[] arr, int min, int max) {
        int range = max - min + 1;
        ht = new HashTable(arr, min, max);

    }

    /**
     * sort the arr with hashTable
     * @param arr
     * @param min
     * @param max
     * @return the new sorted array
     */
    public static int[] sort(int[] arr, int min, int max) {
        int n = arr.length;
        // size is for record which index of the result should
        // i insert value
        int size = 0;
        int[] result = new int[arr.length];
        // put the element in the hashTable
        for (int i = 0; i < arr.length; i++) {
            ht.insert(arr[i]);
        }
        // put the value in that table into array
        for (int i = 0; i < ht.table.length; i++) {
            // if the current linked list is not empty, we
            // put the value into array
            if (ht.table[i] != null && ht.table[i].size() > 0) {
                for (int j = 0; j < ht.table[i].size(); j++) {
                    result[size] = ht.table[i].get(j);
                    size++;
                }
            }
        }
        return result;
    }
}
