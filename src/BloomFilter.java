/*
 * NAME: Zhaoyi Guo
 * PID: A15180402
 */


/**
 * class that implements BloomFilter
 */
public class BloomFilter {
    public static final int NUMBITS = 8;

    byte[] table; // the byte array used as hash table of bits
    int numSlots; // the number of slots (bits) in the hash table
    static int functionFactor = 27;
    static int PJW_HASH_SHIFT = 4;
    static int PJW_HASH_RIGHT_SHIFT = 24;
    static int PJW_HASH_MASK = 0xf0000000;




    /**
     * The constructor that creates a bloom filter of size numBytes in byte and
     * with 8 * numBytes slots (in bits).
     *
     * @param numBytes the number of bytes allocated for the byte array
     */
    public BloomFilter(int numBytes) {

        //constructor that define table and numSlots
        table = new byte[numBytes];
        numSlots = numBytes * NUMBITS;
    }

    /**
     * insert str into the table with three hashfunctions
     * @param str
     */
    public void insert(String str) {

        //three hash functions are called
        // str is set into three indexes in the table
        int index1 = hashVal1(str);
        setBit(index1 / NUMBITS, index1 % NUMBITS);
        int index2 = hashVal2(str);
        setBit(index2 / NUMBITS, index2 % NUMBITS);
        int index3 = hashVal3(str);
        setBit(index3 / NUMBITS, index3 % NUMBITS);

    }

    /**
     * from Sample Hash Function doc
     * @param str
     * @return hashValue % numSlots
     */
    private int hashVal1(String str) {

        //PJW hash function
        int hashValue = 0;
		char[] array = str.toCharArray();
		for (int i = 0; i < array.length; i++)
		{
			hashValue = (hashValue << PJW_HASH_SHIFT) + (int) (array[i]);
			int rotate_bits = hashValue & PJW_HASH_MASK;
			hashValue ^= rotate_bits | (rotate_bits >> PJW_HASH_RIGHT_SHIFT);
		}
		return hashValue % numSlots;

    }

    /**
     * hash function from online source
     * https://www.cpp.edu/~ftang/courses/CS240/lectures/hashing.htm
     * @param str
     * @return hashVallue
     */
    private int hashVal2(String str) {

        //TODO
        char[] array = str.toCharArray();
        int hashValue = 0;
        for (int i = 0; i < array.length; i++) {
            hashValue = (hashValue * functionFactor) + (int)(array[i]);
            hashValue %= numSlots;

        }
        return hashValue;

    }

    /**
     * hash function from Sample Hash Function doc
     * @param str
     * @return Math.abs(hashValue % numSlots)
     */
    private int hashVal3(String str) {

        //WEISS hash function
        int hashValue = 0;
        for (int i = 0; i < str.length(); i++)
            hashValue = hashValue ^ (hashValue << HashTable.WEISS_HASH_SHIFT) ^ str.charAt(i);
        return Math.abs(hashValue % numSlots);

    }


    /**
     * Helper method to set a bit in the table to 1, which is specified by the given byteIndex
     * and bitIndex
     * @param byteIndex the index of the byte in hash table
     * @param bitIndex the index of the bit in the byte at byteIndex. Range is [0, 7]
     */
    private void setBit(int byteIndex, int bitIndex) {
        // set the bit at bitIndex of the byte at byteIndex
        table[byteIndex] = (byte) (table[byteIndex] | ((1 << (NUMBITS - 1)) >> bitIndex));
    }

    /**
     * check whether the str is int the table
     * @param str
     * @return true or false depends on whether the str is found
     */
    public boolean find(String str) {

        //get the three indexes of the str in the table
        int index1 = hashVal1(str);
        int index2 = hashVal2(str);
        int index3 = hashVal3(str);
        // only when str exists in all of the three indexes, it returns true
        if (getSlot(index1 / NUMBITS, index1 % NUMBITS) == 0)
            return false;

        else if (getSlot(index2 / NUMBITS, index2 % NUMBITS) == 0)
            return false;

        else if (getSlot(index3 / NUMBITS, index3 % NUMBITS) == 0)
            return false;
        else
            return true;
    }

    /**
     * Helper method to get the bit value at the slot, which is specified by the given byteIndex
     * and bitIndex
     * @param byteIndex the index of the byte in hash table
     * @param bitIndex the index of the bit in the byte at byteIndex. Range is [0, 7]
     */
    private int getSlot(int byteIndex, int bitIndex) {
        // to get the big value at the slot
        return (table[byteIndex] >> ((NUMBITS - 1) - bitIndex)) & 1;
    }
}
