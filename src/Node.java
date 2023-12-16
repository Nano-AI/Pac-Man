public class Node<T> {
    T val;
    Node<T> next = null;

    public Node(T val) {
        this.val = val;
    }

    public Node(T val, Node<T> next) {
        this(val);
        this.next = next;
    }
}
