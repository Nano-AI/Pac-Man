import java.util.Iterator;

public class LList<T> {
    Node<T> head;
    int size;

    public LList(Node<T> head) {
        this.head = head;
    }

    public LList(T val) {
        this.head = new Node<>(val);
    }

    private Node<T> iterator;

    public LList() {

    }
}
