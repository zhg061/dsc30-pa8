/*
 * NAME: Zhaoyi Guo
 * PID: A15180402
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.*;

public class HashTableTester {
    HashTable test1;
    HashTable test2;
    HashTable test3;
    HashTable test4;
    HashTable test5;
    String property;



    @org.junit.Before
    public void setUp() throws Exception {
        test1 = new HashTable(5);
        test2 = new HashTable(10, "./src/test2.txt");
        test3 = new HashTable(2, "./src/test3.txt");
        test4 = new HashTable(10, "./src/test4.txt");
        test5 = new HashTable(7, "./src/test5.txt");
    }

    @org.junit.Test
    public void insert() {
        test2.insert("apple");
        test2.insert("strawberry");
        test2.insert("banana");
        test2.insert("strawberry");
        test1.insert("apple");
        test1.insert("strawberry");
        test1.insert("banana");
        test3.insert("a");
        test3.insert("b");
        test3.insert("c");
        test3.insert("d");
        File file = new File("./src/simple.dict.txt");
        try {
            Scanner scLine = new Scanner(file);
            while (scLine.hasNextLine()) {
                Scanner scWord = new Scanner(scLine.nextLine());

                if (scWord.hasNext()) {//if the file has next word
                    property = scWord.next();
                    test4.insert(property);
                } else {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Failed to open " + file);
            System.exit(1);
        }
        File file5 = new File("./src/long.dict.txt");
        try {
            Scanner scLine = new Scanner(file5);
            while (scLine.hasNextLine()) {
                Scanner scWord = new Scanner(scLine.nextLine());

                if (scWord.hasNext()) {//if the file has next word
                    property = scWord.next();
                    test5.insert(property);
                } else {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Failed to open " + file);
            System.exit(1);
        }


    }

    @org.junit.Test
    public void delete() {
        test1.insert("apple");
        test1.insert("banana");
        test1.delete("banana");
        test3.insert("a");
        test3.insert("b");
        test3.insert("c");
        test3.insert("d");
        test3.delete("d");
        test3.delete("a");
        test3.delete("b");

    }

    @org.junit.Test
    public void lookup() {
        test1.insert("apple");
        test1.insert("strawberry");
        test1.insert("banana");
        assertTrue(test1.lookup("apple"));
        assertTrue(test1.lookup("banana"));
        test3.insert("a");
        test3.insert("b");
        test3.insert("c");
        test3.insert("d");
        assertFalse(test3.lookup("h"));
    }

    @org.junit.Test
    public void getSize() {
        test1.insert("apple");
        test1.insert("strawberry");
        test1.insert("banana");
        assertEquals(3, test1.getSize());
        test3.insert("a");
        test3.insert("b");
        test3.insert("c");
        test3.insert("d");
        assertEquals(4, test3.getSize());
        test3.delete("d");
        assertEquals(3, test3.getSize());


    }
}