/**
 * The LList class represents a simple linked list implementation and implements the Iterator interface.
 *
 * This class supports generic types (T) for flexibility in storing different data types.
 *
 * @param <T> The type of elements stored in the linked list.
 * @author Aditya Bankoti, Ekam Singh
 * @version January 4th, 2024
 */

import java.util.Iterator;

public class LList<T> implements Iterator {
    Node<T> head;  // Reference to the first node in the linked list
    int size;  // Number of elements in the linked list
    Node<T> iterate;  // Current node used for iteration

    /**
     * Constructs an LList with the specified head node.
     *
     * @param head The head node of the linked list.
     */
    public LList(Node<T> head) {
        this.head = head;
    }

    /**
     * Constructs an LList with a single element.
     *
     * @param val The value of the single element in the linked list.
     */
    public LList(T val) {
        this.head = new Node<>(val);
    }

    // Additional fields
    private Node<T> iterator;  // Iterator for traversing the linked list

    /**
     * Default constructor for an empty linked list.
     */
    public LList() {

    }

    /**
     * Checks if there are more elements in the linked list during iteration.
     *
     * @return true if there are more elements, false otherwise.
     */
    @Override
    public boolean hasNext() {
        return iterator.next != null;
    }

    /**
     * Retrieves the next element during iteration.
     *
     * @return The next element in the linked list.
     */
    @Override
    public Object next() {
        if (!hasNext()) {
            iterator = head;  // Reset iterator to the head if at the end
            return iterator;
        }
        iterator = iterator.next;  // Move iterator to the next node
        return iterator;
    }
}
