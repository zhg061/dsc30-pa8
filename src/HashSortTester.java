import org.junit.Test;

import java.sql.SQLOutput;

import static org.junit.Assert.*;

public class HashSortTester {
    HashSort test1;
    int[] result1;
    HashSort test2;
    int[] result2;

    @org.junit.Before
    public void setUp() throws Exception {
        test1 = new HashSort(new int[] {4, 5, 6, 1, 8, 100}, 1,  100);
        test2 = new HashSort(new int[] {4, 6, 2, 7, 1, 66, 77, 88, 55, 100}, 1,  100);

    }

    @Test
    public void sort() {
        result1 = test1.sort(new int[] {4, 5, 6, 1, 8, 100}, 1,  100);
        assertArrayEquals(new int[] {1, 4, 5, 6, 8, 100}, result1);
    }
}