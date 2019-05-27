import javax.xml.soap.Node;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractList;

/**
 * Design and implement a data structure for Least Recently Used (LRU) cache
 */
public class LRUCache {
    private HashMap<Integer, Node> map;
    private int capacity ;
    private int count;
    private Node head;
    private Node tail;
    private class Node {
        int key;
        int value;
        Node pre;
        Node next;

        public Node(int key, int value)
        {
            this.key = key;
            this.value = value;
        }
    }
    /**
     * a single constructor which will
     * take the capacity(int) of the cache as its parameter.
     * @param capacity
     */
    public LRUCache(int capacity) {
        map = new HashMap<>();
        this.capacity = capacity;
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        head.pre = null;
        tail.pre = head;
        tail.next = null;
        count = 0;
    }

    public void delete(Node node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
    }

    public void addToHead(Node node) {
        node.next = head.next;
        node.next.pre = node;
        node.pre = head;
        head.next = node;
    }
    /**
     * Return the value (would always be positive)
     * of the key if the key exists in the cache,
     * otherwise return -1
     * @param key
     * @return
     */

    public int get(int key) {
        int keyCopy = key;

        // if the map does not contain the key,
        // or the value at key is negative, return -1
        if(map.get(keyCopy) == null || map.get(keyCopy).value < 0
                 || !map.containsKey(keyCopy))
            return -1;
        // get the value at the key
        Node value = map.get(keyCopy);
        delete(value);
        addToHead(value);

        return value.value;
    }

    /**
     * Set the value of the key,
     * if the key does not exist in the cache.
     * When the cache reached its capacity,
     * it should invalidate the least recently
     * used item before inserting a new item
     * @param key
     * @param value
     */
    public void set(int key, int value) {
//        if (value < 0)
//            return;
        int keyCopy = key;
        Node valueCopy = new Node(key, value);

        //if the key is not in the map
        if(!map.containsKey(keyCopy)) {
            map.put(key, valueCopy);
            //remove the least recently used frame when the cache is full
            if(count == capacity) {
                // not sure to remove tail.prev or tail.prev.key
                map.remove(tail.pre.key);
                delete(tail.pre);
                addToHead(valueCopy);

            }
            else {
                count++;
                addToHead(valueCopy);
            }
        }
        // if the key is in the map
        else {
            Node node = map.get(keyCopy);
            delete(node);
            map.replace(keyCopy,  valueCopy);
            node.value = value;
//            map.remove(map.get(key));
            addToHead(valueCopy);
//            map.put(keyCopy, valueCopy);

        }
        }


    }



