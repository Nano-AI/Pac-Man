/**
 * The Node class represents a node in a linked list.
 *
 * This class supports generic types (T) for flexibility in storing different data types.
 *
 * @param <T> The type of data stored in the node.
 * @author Aditya Bankoti, Ekam Singh
 * @version January 4th, 2024
 */
public class Node<T> {
    T val;  // Data stored in the node
    Node<T> next = null;  // Reference to the next node in the linked list

    /**
     * Constructs a Node with the specified value.
     *
     * @param val The value to be stored in the node.
     */
    public Node(T val) {
        this.val = val;
    }

    /**
     * Constructs a Node with the specified value and reference to the next node.
     *
     * @param val   The value to be stored in the node.
     * @param next  Reference to the next node in the linked list.
     */
    public Node(T val, Node<T> next) {
        this(val);
        this.next = next;
    }
}
