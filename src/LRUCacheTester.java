import org.junit.Test;

import static org.junit.Assert.*;

public class LRUCacheTester {
    LRUCache test1;
    LRUCache test2;
    @org.junit.Before
    public void setUp() throws Exception {
        test1 = new LRUCache(2);
        test2 = new LRUCache(3);
    }

    @Test
    public void get() {
        test2.set(1, 1);
        test2.set(2, 1);
        test2.set(3, 2);
        test2.set(4, 5);
        assertEquals(-1, test2.get(1));
        assertEquals(2, test2.get(3));
        assertEquals(1, test2.get(2));
        assertEquals(5, test2.get(4));
        test2.set(1, 1);
        test2.set(2, 1);
        assertEquals(1, test2.get(2));
        assertEquals(1, test2.get(1));
        assertEquals(-1, test2.get(3));
        assertEquals(5, test2.get(4));
        test2.set(5, 1);
        test2.set(1, 1);
        test2.set(2, 1);
        test2.set(3, 1);
        assertEquals(1, test2.get(2));
        assertEquals(1, test2.get(3));
        assertEquals(1, test2.get(1));
        assertEquals(-1, test2.get(5));
        test2.set(4, 1);
        test2.set(5, 1);
        assertEquals(-1, test2.get(3));
        assertEquals(1, test2.get(4));
        assertEquals(1, test2.get(5));


    }

    @Test
    public void set() {
        test1.set(1, 10);
        test1.set(2, 20);
        assertEquals(10, test1.get(1));
        test1.set(3, 30);
        assertEquals(-1, test1.get(2));
        test1.set(4, 40);
        assertEquals(-1, test1.get(1));
        assertEquals(30, test1.get(3));
        assertEquals(40, test1.get(4));
    }
}