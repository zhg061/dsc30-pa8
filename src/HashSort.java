import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.LinkedList;

public class HashSort {
    private static HashTable ht;
    private int[] arr;
    private int min, max, bucketSize, n, range;

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
        final double loadStandard =(double)2/ (double)3;
        final int expandFactor = 2;
        private int longestChain;
        DecimalFormat df = new DecimalFormat("#.##");
        final int functionFactor = 27;
        final int WORD_WIDTH = 4 * NUMBITS;
        final int PREISS_HASH_SHIFT = 6;
        final int WEISS_HASH_SHIFT = 5;




        /**
         * The constructor that creates a hash table of a given size.
         * If a user uses this constructor, printStats should be set
         * to false and do not use printTable() to
         * print the stats before each resizing.
         * @param size
         */
        public HashTable(int size) {

            //Initialize
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
        public HashTable(int size, String fileName){

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
        public boolean insert(int value) {

            //TODO

            loadFactor = (double)nelems / (double)table.length;
            if (loadFactor > loadStandard)
                rehash();
            if (value <= 0)
                throw new NullPointerException();
            // get the index by using the hash function
            int curIndex = hashVal(value);
            if(table[curIndex] == null) {
                table[curIndex] = new LinkedList<>();
            }
            if(table[curIndex].size()>0) {
                collision++;
            }
            nelems++;
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
        public boolean delete(int value) {

            //TODO
            if (value <= 0)
                throw new NullPointerException();
            int curIndex = hashVal(value);
            if(lookup(value)) {
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
         * @return
         */
        public boolean lookup(int value) {

            //TODO
            if (value <= 0)
                throw new NullPointerException();
            int curIndex = hashVal(value);
            if(table[curIndex] == null || !table[curIndex].contains(value)) {
                return false;
            }
            return true;
        }



        /**
         * Print out the hash table
         */
        public void printTable() {

            //TODO
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

            //TODO
            return nelems;
        }

        /**
         * Return the hash value of the given string.
         * @param str
         * @return
         */
        private int hashVal(int str) {

            //TODO

//            char[] array = str.toCharArray();
            int hashValue = 0;
            for (int i = 0; i < str; i++) {
                hashValue = (hashValue * functionFactor) + (int) (array[i]);
                hashValue %= table.length;
            }
            return hashValue;
        }

        /**
         * Double the size of hash table and rehash all the entries.
         */
        private void rehash() {

            //TODO

            if (printStats)
                printStatistics();
            LinkedList<String>[] table1 = table;
            table = new LinkedList[table.length * expandFactor];
            collision = 0;
            nelems = 0;
            for (int i = 0; i < table1.length; i++) {
                if(table1[i] == null)
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
            String loadFactor1= df.format(loadFactor);
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

    public HashSort(int[] arr, int min, int max) {
        this.n = arr.length;
        this.arr = arr;
        this.min = min;
        this.max = max;
        this.range = max - min;
        this.bucketSize = (range + n - 1) / n;
        ht = new HashTable(range);
    }
    public static int[] sort(int[] arr, int min, int max) {
        int[] result = new int[max - min];
        for (int i = 0; i < arr.length; i++) {
            ht.insert(arr[i]);
        }
        for (int i = 0; i < arr.length; i++) {
            int j = i;
            // compare the previous list element to the current list element
            // if the previous element is greater than the current element, swap them
            while (j > 0 && arr[j] > arr[j - 1]) {
                int temp = arr[j];
                arr[j] = arr[j - 1];
                arr[j - 1] = temp;
                // decrease j by 1, and maybe iterate the while loop again
                j--;
            }
//            for (int m = 0; m < )
        }
        return result;
    }

}
